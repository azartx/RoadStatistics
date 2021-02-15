package by.roadstatistics.services

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_NONE
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.graphics.Color
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.os.Looper
import android.util.Log
import androidx.core.app.NotificationCompat.Builder
import androidx.core.app.NotificationCompat.CATEGORY_SERVICE
import androidx.core.app.NotificationCompat.PRIORITY_MAX
import androidx.core.app.NotificationCompat.VISIBILITY_PRIVATE
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationAvailability
import com.google.android.gms.location.FusedLocationProviderClient

class LocService : Service() {

    private val bindService: BindService = BindService()
    private lateinit var locationProvider: FusedLocationProviderClient

    private fun showServiceNotification() {
        createNotificationChannel()
        Builder(baseContext, "myService")
            .setContentTitle("Location service is now run.")
            .setContentText("Running statistic read your location and confirming to you statistics.")
            .setCategory(CATEGORY_SERVICE)
            .setPriority(PRIORITY_MAX)
            .build().apply {
                startForeground(101, this)
            }
    }

    // if sdk.level >= api26 then create notification channel
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel("myService", "locService", IMPORTANCE_NONE).apply {
                lightColor = Color.BLUE
                lockscreenVisibility = VISIBILITY_PRIVATE
                (getSystemService(NOTIFICATION_SERVICE) as NotificationManager)
                    .createNotificationChannel(this)
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        Log.i("FFFF", "service is start")
    }

    @SuppressLint("MissingPermission")
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        showServiceNotification()

        locationProvider = LocationServices.getFusedLocationProviderClient(this)
        if (checkLocationPermission()) {
            locationProvider = LocationServices.getFusedLocationProviderClient(this)
            locationProvider.lastLocation.addOnCompleteListener {

                Log.i("FFFF", it.toString())

            }
            requestUserLocation()
        }

        return START_STICKY
    }

    private fun checkLocationPermission() =
        ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) == PERMISSION_GRANTED

    override fun onBind(intent: Intent?): IBinder = bindService

    inner class BindService() : Binder() {
        // TODO Some code
    }

    @SuppressLint("MissingPermission")
    private fun requestUserLocation() {
        val locationRequest = LocationRequest.create()
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
        locationProvider.requestLocationUpdates(
            locationRequest, object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult?) {
                    super.onLocationResult(locationResult)
                    Log.i("FFFF", locationResult?.locations.toString())
                }

                override fun onLocationAvailability(p0: LocationAvailability?) {
                    super.onLocationAvailability(p0)
                }
            },
            Looper.getMainLooper()
        )
    }

}
