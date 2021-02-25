package by.roadstatistics.services

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_NONE
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.graphics.Color
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkInfo
import android.net.wifi.p2p.WifiP2pManager
import android.os.Build
import android.os.IBinder
import android.os.Binder
import android.util.Log
import androidx.core.app.NotificationCompat.*
import androidx.core.content.ContextCompat
import by.roadstatistics.R
import by.roadstatistics.database.CordInfo
import by.roadstatistics.database.DatabaseRepository
import com.google.android.gms.location.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.withContext
import kotlinx.coroutines.launch
import java.lang.Exception
import java.lang.NullPointerException
import java.net.InetAddress
import java.util.Calendar
import java.util.concurrent.Executors

class LocService : Service() {

    private val bindService: BindService = BindService()
    private lateinit var locationProvider: FusedLocationProviderClient
    private lateinit var databaseRepository: DatabaseRepository
    private val cal = Calendar.getInstance()
    private val CHANNEL_ID = "myChannel"
    private val executorService = Executors.newSingleThreadExecutor()

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        showServiceNotification()
        databaseRepository = DatabaseRepository(baseContext)

        CoroutineScope(Dispatchers.Main + Job()).launch {
            withContext(Dispatchers.Unconfined) {
                getCordsLogic()


                /*while (true) {
                    if (checkLocationPermission()) {
                        locationProvider =
                            LocationServices.getFusedLocationProviderClient(baseContext)
                        locationProvider.lastLocation.addOnCompleteListener { location ->

                            val cal = Calendar.getInstance()
                            var lat = location.result.latitude.toFloat()
                            var lon = location.result.longitude.toFloat()

                            databaseRepository = DatabaseRepository(baseContext)
                            databaseRepository.addCordsToDatabase(
                                CordInfo(
                                    year = cal.get(Calendar.YEAR),
                                    month = cal.get(Calendar.MONTH) + 1,
                                    day = cal.get(Calendar.DAY_OF_MONTH),
                                    hours = cal.get(Calendar.HOUR_OF_DAY),
                                    minutes = cal.get(Calendar.MINUTE),
                                    latitude = lat,
                                    longitude = lon
                                )
                            )


                        }
                    }
                    delay(5000)
                }*/

            }
        }
        return START_STICKY
    }

    private fun getCordsLogic() {
        if (checkLocationPermission()) {
            // этот вызов отработает раз, добавит координат сразу
            try {
                locationProvider =
                    LocationServices.getFusedLocationProviderClient(baseContext)
                locationProvider.lastLocation.addOnCompleteListener { loc ->
                    databaseRepository.addCordsToDatabase(
                        CordInfo(
                            year = cal.get(Calendar.YEAR),
                            month = cal.get(Calendar.MONTH) + 1,
                            day = cal.get(Calendar.DAY_OF_MONTH),
                            hours = cal.get(Calendar.HOUR_OF_DAY),
                            minutes = cal.get(Calendar.MINUTE),
                            latitude = loc.result.latitude.toFloat(),
                            longitude = loc.result.longitude.toFloat()
                        )
                    )
                }
            } catch (e: NullPointerException) {
                Log.i("LOG", "NPE: provider has no cords. Exception log:\n$e")
            }


            // этот вызов будет писать в базу по изменению локации
            val los = getSystemService(LOCATION_SERVICE) as LocationManager
            if (checkInternetConnection() && checkGpsIsOn()) {
                los.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5F) { loc ->
                    databaseRepository.addCordsToDatabase(
                        CordInfo(
                            year = cal.get(Calendar.YEAR),
                            month = cal.get(Calendar.MONTH) + 1,
                            day = cal.get(Calendar.DAY_OF_MONTH),
                            hours = cal.get(Calendar.HOUR_OF_DAY),
                            minutes = cal.get(Calendar.MINUTE),
                            latitude = loc.latitude.toFloat(),
                            longitude = loc.longitude.toFloat()
                        )
                    )
                }
            } else if (checkInternetConnection() && !checkGpsIsOn()) {
                los.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    5000,
                    5F
                ) { loc ->
                    databaseRepository.addCordsToDatabase(
                        CordInfo(
                            year = cal.get(Calendar.YEAR),
                            month = cal.get(Calendar.MONTH) + 1,
                            day = cal.get(Calendar.DAY_OF_MONTH),
                            hours = cal.get(Calendar.HOUR_OF_DAY),
                            minutes = cal.get(Calendar.MINUTE),
                            latitude = loc.latitude.toFloat(),
                            longitude = loc.longitude.toFloat()
                        )
                    )
                }
            } else if (!checkInternetConnection()) {
                //showLostInternetNotification()
            }
        }
    }

    // if sdk.level >= api26 then create notification channel
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel(
                CHANNEL_ID,
                getString(R.string.notification_location_name),
                IMPORTANCE_NONE
            ).apply {
                lightColor = Color.BLUE
                lockscreenVisibility = VISIBILITY_PRIVATE
                (getSystemService(NOTIFICATION_SERVICE) as NotificationManager)
                    .createNotificationChannel(this)
            }
        }
    }

    private fun showServiceNotification() {
        Builder(baseContext, CHANNEL_ID)
            .setContentTitle(getString(R.string.notificationLocationTitle))
            .setContentText(getString(R.string.notification_location_description))
            .setSmallIcon(R.mipmap.icon)
            .setCategory(CATEGORY_SERVICE)
            .setPriority(PRIORITY_MAX)
            .build().apply {
                startForeground(101, this)
            }
    }

    private fun showLostInternetNotification() {
        Builder(baseContext, CHANNEL_ID)
            .setContentTitle(getString(R.string.notificationLostInternetConnectionTitle))
            .setContentText(getString(R.string.notificationLostInternetConnection))
            .setCategory(CATEGORY_MESSAGE)
            .setSmallIcon(R.mipmap.icon)
            .setPriority(PRIORITY_MIN)
            .build().apply {
                startForeground(102, this)
            }

        do {
            if (checkInternetConnection()) {
                getSystemService(NOTIFICATION_SERVICE).apply {
                    this as NotificationManager
                    this.cancel(101)
                }
                getCordsLogic()
                break
            }
        } while (!checkInternetConnection())


    }

    private fun checkInternetConnection(): Boolean {
        /*return try {
            val checkConnection = InetAddress.getByName("https://google.com/")
            checkConnection.equals("")
        } catch (e: Exception) {
            Log.i("FFFF", e.toString())
            false
        }*/
        return true

    }

    private fun checkGpsIsOn(): Boolean {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    private fun checkLocationPermission() =
        ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) == PERMISSION_GRANTED

    override fun onBind(intent: Intent?): IBinder = bindService
    inner class BindService() : Binder() {
        // TODO Some code
    }

}
