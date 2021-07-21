package com.varpihovsky.jetiq

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.*
import com.varpihovsky.jetiq.services.SessionRestorationWorker
import dagger.hilt.android.HiltAndroidApp
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltAndroidApp
class JetIQApplication : Application(), Configuration.Provider {
    // Hilt work manager integration
    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    private val workManager by lazy { WorkManager.getInstance(this) }

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
        val request = buildPeriodicWorkRequest(
            SESSION_RESTORATION_INTERVAL,
            SESSION_RESTORATION_FLEX_INTERVAL,
            TimeUnit.HOURS,
            networkConstraints
        )

        workManager.enqueueUniquePeriodicWork(
            SESSION_RESTORATION_WORK_TAG,
            ExistingPeriodicWorkPolicy.KEEP,
            request
        )
    }

    private fun enqueueNotificationWorker(networkConstraints: Constraints) {
        val request = buildPeriodicWorkRequest(
            NOTIFICATION_INTERVAL,
            NOTIFICATION_FLEX_INTERVAL,
            TimeUnit.MINUTES,
            networkConstraints
        )

        workManager.enqueueUniquePeriodicWork(
            NOTIFICATION_WORK_TAG,
            ExistingPeriodicWorkPolicy.KEEP,
            request
        )
    }

    private fun buildPeriodicWorkRequest(
        interval: Long,
        flexInterval: Long,
        timeUnit: TimeUnit,
        constraints: Constraints
    ) = PeriodicWorkRequestBuilder<SessionRestorationWorker>(
        interval, timeUnit,
        flexInterval, timeUnit
    ).setConstraints(constraints)
        .build()

    companion object {
        private const val SESSION_RESTORATION_INTERVAL = 4L
        private const val SESSION_RESTORATION_FLEX_INTERVAL = 2L
        private const val SESSION_RESTORATION_WORK_TAG = "session_restoration"

        private const val NOTIFICATION_INTERVAL = 15L
        private const val NOTIFICATION_FLEX_INTERVAL = 5L
        private const val NOTIFICATION_WORK_TAG = "notifications"
    }
}