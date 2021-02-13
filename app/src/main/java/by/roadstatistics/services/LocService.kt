package by.roadstatistics.services

import android.content.Intent
import android.os.IBinder
import androidx.lifecycle.LifecycleService

class LocService(): LifecycleService() {

    override fun onBind(intent: Intent?): IBinder? {
        return super.onBind(intent)



    }
}