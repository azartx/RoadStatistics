package by.roadstatistics.services

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.annotation.SuppressLint
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Binder
import android.os.IBinder
import android.os.Looper
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.*


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
        locationProvider.requestLocationUpdates(locationRequest, object : LocationCallback() {
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
