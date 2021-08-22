package com.varpihovsky.jetiq

import com.varpihovsky.core.di.CoreModule
import com.varpihovsky.core.log.Logger
import com.varpihovsky.core_db.DatabaseModule
import com.varpihovsky.core_nav.NavigationModule
import com.varpihovsky.core_repo.RepoModule
import com.varpihovsky.feature_auth.AuthModule
import com.varpihovsky.feature_contacts.ContactsModule
import com.varpihovsky.feature_messages.MessagesModule
import com.varpihovsky.feature_new_message.NewMessageModule
import com.varpihovsky.feature_profile.ProfileModule
import com.varpihovsky.feature_settings.SettingsModule
import com.varpihovsky.feature_subjects.SubjectsModule
import com.varpihovsky.jetiq.di.ApplicationModule
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.logger.Level

fun main() {
    Logger.ui("Starting application")
    Thread.setDefaultUncaughtExceptionHandler { t, e ->
        Logger.e(t::class, e.stackTraceToString())
    }
    initDi()
    Application().run()
    stopKoin()
}

private fun initDi() {
    startKoin {
        printLogger(Level.DEBUG)
        modules(modules)
    }
}

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