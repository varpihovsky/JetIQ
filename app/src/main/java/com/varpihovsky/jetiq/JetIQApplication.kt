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
package com.varpihovsky.jetiq

import android.app.Application
import androidx.work.*
import com.varpihovsky.core.di.CoreModule
import com.varpihovsky.core_db.DatabaseModule
import com.varpihovsky.core_nav.NavigationModule
import com.varpihovsky.core_repo.RepoModule
import com.varpihovsky.core_repo.repo.MessagesRepo
import com.varpihovsky.core_repo.repo.SubjectRepo
import com.varpihovsky.core_repo.repo.UserPreferencesRepo
import com.varpihovsky.feature_auth.AuthModule
import com.varpihovsky.feature_contacts.ContactsModule
import com.varpihovsky.feature_messages.MessagesModule
import com.varpihovsky.feature_new_message.NewMessageModule
import com.varpihovsky.feature_profile.ProfileModule
import com.varpihovsky.feature_settings.SettingsModule
import com.varpihovsky.feature_subjects.SubjectsModule
import com.varpihovsky.jetiq.di.ApplicationModule
import com.varpihovsky.jetiq.services.SessionRestorationWorker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.workmanager.koin.workManagerFactory
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.startKoin
import java.util.concurrent.TimeUnit

class JetIQApplication : Application(), KoinComponent {
    private val userPreferencesRepo: UserPreferencesRepo by inject()

    private val workManager by lazy { WorkManager.getInstance(this) }

    private val modules = listOf(
        ApplicationModule.module,
        CoreModule.module,
        DatabaseModule.module,
        NavigationModule.module,
        RepoModule.module,
        AuthModule.module,
        ContactsModule.module,
        MessagesModule.module,
        NewMessageModule.module,
        ProfileModule.module,
        SettingsModule.module,
        SubjectsModule.module
    )

    override fun onCreate() {
        super.onCreate()
        initDi()
        scheduleBackgroundWork()
        CoroutineScope(Dispatchers.IO).launch {
            startLoading()
            collectNotificationSettings()
        }
    }

    private fun initDi() {
        startKoin {
            androidContext(this@JetIQApplication)
            workManagerFactory()
            modules(modules)
        }
    }

    private suspend fun startLoading() {
        // Injecting in this place to not hold link
        val subjectRepo: SubjectRepo by inject()
        subjectRepo.load()

        val messagesRepo: MessagesRepo by inject()
        messagesRepo.loadMessages()
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