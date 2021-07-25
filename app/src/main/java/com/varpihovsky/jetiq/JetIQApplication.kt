package com.varpihovsky.jetiq

/* JetIQ
 * Copyright © 2021 Vladyslav Podrezenko
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.*
import com.varpihovsky.core_repo.repo.UserPreferencesRepo
import com.varpihovsky.jetiq.services.SessionRestorationWorker
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltAndroidApp
class JetIQApplication : Application(), Configuration.Provider {
    // Hilt work manager integration
    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    @Inject
    lateinit var userPreferencesRepo: UserPreferencesRepo

    private val workManager by lazy { WorkManager.getInstance(this) }

    override fun getWorkManagerConfiguration(): Configuration =
        Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

    override fun onCreate() {
        super.onCreate()

        scheduleBackgroundWork()
        CoroutineScope(Dispatchers.IO).launch { collectNotificationSettings() }
    }

    private suspend fun collectNotificationSettings() {
        userPreferencesRepo.flow.map { it.showNotifications }.collect {
            if (it) {
                scheduleBackgroundWork()
            } else {
                workManager.cancelAllWorkByTag(NOTIFICATION_WORK_TAG)
            }
        }
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