package by.roadstatistics.services

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_NONE
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.graphics.BitmapFactory
import android.graphics.Color
import android.location.LocationManager
import android.os.Build
import android.os.IBinder
import android.os.Binder
import android.util.Log
import androidx.core.app.NotificationCompat.VISIBILITY_PRIVATE
import androidx.core.app.NotificationCompat.Builder
import androidx.core.app.NotificationCompat.CATEGORY_SERVICE
import androidx.core.app.NotificationCompat.BigTextStyle
import androidx.core.app.NotificationCompat.PRIORITY_MAX
import androidx.core.app.NotificationCompat.CATEGORY_MESSAGE
import androidx.core.app.NotificationCompat.PRIORITY_MIN
import androidx.core.content.ContextCompat
import by.roadstatistics.R
import by.roadstatistics.database.CordInfo
import by.roadstatistics.database.DatabaseRepository
import by.roadstatistics.database.firebase.FirebaseRepository
import by.roadstatistics.utils.Constants.USER_ID
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.withContext
import kotlinx.coroutines.launch
import java.lang.NullPointerException
import java.net.InetAddress
import java.util.Calendar

/**
 * Custom location service, catch all of the user cords and send to database repository
 */

class LocService : Service() {

    private val bindService: BindService = BindService()
    private lateinit var locationProvider: FusedLocationProviderClient
    private lateinit var databaseRepository: DatabaseRepository
    private val cal = Calendar.getInstance()
    private val channelId = "myChannel"
    private val firebaseRepository = FirebaseRepository()

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
                    if (loc.result != null) {
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

                        if (USER_ID != "0") {
                            firebaseRepository.updateChildren(
                                loc.result.latitude,
                                loc.result.longitude
                            )
                        }
                    }
                }
            } catch (e: NullPointerException) {
                Log.i("LOG", "NPE: provider has no cords. Exception log:\n$e")
            }

            // этот вызов будет писать в базу по изменению локации
            val los = getSystemService(LOCATION_SERVICE) as LocationManager
            if (/*checkInternetConnection() && checkGpsIsOn()*/true) {
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
                    if (USER_ID != "0") {
                        firebaseRepository.updateChildren(loc.latitude, loc.longitude)
                    }

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
                    if (USER_ID != "0") {
                        firebaseRepository.updateChildren(loc.latitude, loc.longitude)
                    }
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
                channelId,
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

    // general service notification
    private fun showServiceNotification() {
        Builder(baseContext, channelId)
            .setContentTitle(getString(R.string.notificationLocationTitle))
            .setContentText(getString(R.string.notification_location_description))
            .setSmallIcon(R.drawable.ic_notif_small)
            .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.ic_notif_large))
            .setCategory(CATEGORY_SERVICE)
            .setStyle(
                BigTextStyle().bigText(
                    getString(R.string.notification_location_description) + getString(
                        R.string.notification_large_description
                    )
                )
            )
            .setPriority(PRIORITY_MAX)
            .build().apply {
                startForeground(101, this)
            }
    }

    // if internet connection is lost, then this notification is show
    private fun showLostInternetNotification() {
        Builder(baseContext, channelId)
            .setContentTitle(getString(R.string.notificationLostInternetConnectionTitle))
            .setContentText(getString(R.string.notificationLostInternetConnection))
            .setCategory(CATEGORY_MESSAGE)
            .setSmallIcon(R.drawable.ic_notif_small)
            .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.ic_notif_large))
            .setPriority(PRIORITY_MIN)
            .build().apply {
                startForeground(102, this)
            }

        // checking internet connection again loop
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

    // attention, main thread!
    private fun checkInternetConnection(): Boolean {
        return try {
            val checkConnection = InetAddress.getByName("https://google.com/")
            checkConnection.equals("")
        } catch (e: Exception) {
            Log.i("FFFF", "Check internet loop catch next exception: $e")
            false
        }
    }

    private fun checkGpsIsOn(): Boolean {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    private fun checkLocationPermission() =
        ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) == PERMISSION_GRANTED

    override fun onBind(intent: Intent?): IBinder = bindService
    inner class BindService : Binder() {
        // if need to call service into the activity on some els, need to use this class
    }

}