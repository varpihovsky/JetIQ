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

    private val workmanager by lazy { WorkManager.getInstance(this) }

    override fun getWorkManagerConfiguration(): Configuration =
        Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

    override fun onCreate() {
        super.onCreate()

        scheduleBackgroundWork()
    }

    private fun scheduleBackgroundWork() {
        Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
            .let {
                enqueueSessionRestoration(it)
                enqueueNotificationWorker(it)
            }
    }

    private fun enqueueSessionRestoration(networkConstraints: Constraints) {
        val request = PeriodicWorkRequestBuilder<SessionRestorationWorker>(
            SESSION_RESTORATION_INTERVAL, TimeUnit.HOURS,
            SESSION_RESTORATION_FLEX_INTERVAL, TimeUnit.HOURS
        ).setConstraints(networkConstraints)
            .build()

        workmanager.enqueueUniquePeriodicWork(
            SESSION_RESTORATION_WORK_TAG,
            ExistingPeriodicWorkPolicy.KEEP,
            request
        )
    }

    private fun enqueueNotificationWorker(networkConstraints: Constraints) {
        val request = PeriodicWorkRequestBuilder<SessionRestorationWorker>(
            NOTIFICATION_INTERVAL, TimeUnit.MINUTES,
            NOTIFICATION_FLEX_INTERVAL, TimeUnit.MINUTES
        ).setConstraints(networkConstraints)
            .build()

        workmanager.enqueueUniquePeriodicWork(
            NOTIFICATION_WORK_TAG,
            ExistingPeriodicWorkPolicy.KEEP,
            request
        )
    }

    companion object {
        private const val SESSION_RESTORATION_INTERVAL = 4L
        private const val SESSION_RESTORATION_FLEX_INTERVAL = 2L
        private const val SESSION_RESTORATION_WORK_TAG = "session_restoration"

        private const val NOTIFICATION_INTERVAL = 15L
        private const val NOTIFICATION_FLEX_INTERVAL = 5L
        private const val NOTIFICATION_WORK_TAG = "notifications"
    }
}