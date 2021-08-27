package com.varpihovsky.jetiq

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.jetbrains.lifecycle.LifecycleController
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.varpihovsky.core.di.CoreModule
import com.varpihovsky.core_db.DatabaseModule
import com.varpihovsky.core_repo.RepoModule
import com.varpihovsky.core_repo.repo.MessagesRepo
import com.varpihovsky.core_repo.repo.SubjectRepo
import com.varpihovsky.feature_auth.AuthModule
import com.varpihovsky.feature_profile.ProfileModule
import com.varpihovsky.jetiq.di.ApplicationModule
import com.varpihovsky.ui_root.root.Root
import com.varpihovsky.ui_root.root.RootComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.GlobalContext
import org.koin.core.context.stopKoin

class Application : KoinComponent {
    private var isRunning by mutableStateOf(true)

    private val lifecycleRegistry = LifecycleRegistry()

    private val subjectRepo: SubjectRepo by inject()
    private val messagesRepo: MessagesRepo by inject()

    private val modules = listOf(
        ApplicationModule.module,
        CoreModule.module,
        DatabaseModule.module,
        RepoModule.module,
        AuthModule.module,
        ProfileModule.module,
    )

    fun run() {
        initDi()
        CoroutineScope(Dispatchers.IO).launch {
            subjectRepo.load()
            messagesRepo.loadMessages()
        }
        initUi()
        stopKoin()
    }

    private fun initDi() {
        GlobalContext.startKoin {
            //printLogger(Level.DEBUG)
            modules(modules)
        }
    }

    @OptIn(ExperimentalDecomposeApi::class)
    private fun initUi() = application {
        val rootComponent = RootComponent(DefaultComponentContext(lifecycleRegistry))
        val windowState = rememberWindowState()

        LifecycleController(lifecycleRegistry, windowState)

        if (isRunning) {
            Window(
                state = windowState,
                onCloseRequest = { isRunning = false }
            ) {
                Root(rootComponent)
            }
        } else {
            exitApplication()
        }
    }

}