package by.roadstatistics.services

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Binder
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices


class LocService() : Service() {

    private val bindService: BindService = BindService()
    private lateinit var locationProvider: FusedLocationProviderClient

    private fun showServiceNotification() {
        NotificationCompat.Builder(baseContext, "CHANNEL_ID")
            .setContentTitle("Location service is now run.")
            .setContentText("Running statistic read your location and confirming to you statistics.")
            .build().apply {
                startForeground(1, this)
            }

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        locationProvider = LocationServices.getFusedLocationProviderClient(this)
        if (checkLocationPermission()) {

        }

        return START_STICKY
    }

    private fun checkLocationPermission() =
        ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) == PERMISSION_GRANTED

    override fun onBind(intent: Intent?): IBinder = bindService

    inner class BindService() : Binder() {
        // TODO Some code
    }

}
