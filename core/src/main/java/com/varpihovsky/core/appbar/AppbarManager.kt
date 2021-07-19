package com.varpihovsky.core.appbar

import com.varpihovsky.core.eventBus.EventBus
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.mapNotNull
import javax.inject.Inject

class AppbarManager @Inject constructor(private val eventBus: EventBus) {
    val commands = eventBus.bus.mapNotNull { it as? AppbarCommand }.distinctUntilChanged()

    fun manage(command: AppbarCommand) {
        eventBus.push(command)
    }
}