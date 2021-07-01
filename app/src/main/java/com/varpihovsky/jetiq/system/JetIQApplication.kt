package com.varpihovsky.jetiq.system

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.*
import com.varpihovsky.jetiq.system.services.SessionRestorationWorker
import dagger.hilt.android.HiltAndroidApp
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltAndroidApp
class JetIQApplication : Application(), Configuration.Provider {
    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override fun getWorkManagerConfiguration(): Configuration =
        Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

    override fun onCreate() {
        super.onCreate()

        scheduleBackgroundWork()
    }

    private fun scheduleBackgroundWork() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresCharging(false)
            .setRequiresBatteryNotLow(false)
            .build()

        val request = PeriodicWorkRequestBuilder<SessionRestorationWorker>(
            SESSION_RESTORATION_INTERVAL, TimeUnit.HOURS,
            SESSION_RESTORATION_FLEX_INTERVAL, TimeUnit.HOURS
        ).setConstraints(constraints)
            .build()

        WorkManager.getInstance(this).also {
            it.cancelAllWork()
            it.enqueue(request)
        }
    }

    companion object {
        private const val SESSION_RESTORATION_INTERVAL = 4L
        private const val SESSION_RESTORATION_FLEX_INTERVAL = 2L
    }
}