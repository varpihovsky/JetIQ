package com.varpihovsky.jetiq

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.varpihovsky.core.appbar.AppbarManager
import com.varpihovsky.core.di.getViewModel
import com.varpihovsky.core.eventBus.EventBus
import com.varpihovsky.core.exceptions.ExceptionEventManager
import com.varpihovsky.jetiq.ui.Root
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class Application : KoinComponent {
    private var isRunning by mutableStateOf(true)

    private val appbarManager: AppbarManager by inject()
    private val exceptionEventManager: ExceptionEventManager by inject()
    private val eventBus: EventBus by inject()

    fun run() {
        initUi()
    }

    private fun initUi() = application {
        if (isRunning) {
            Window(
                onCloseRequest = { isRunning = false }
            ) {
                Root(
                    navigationViewModel = getViewModel(),
                    appbarManager = appbarManager,
                    exceptionEventManager = exceptionEventManager,
                    eventBus = eventBus
                )
            }
        }
    }

}