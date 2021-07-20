package com.varpihovsky.core.appbar

import com.varpihovsky.core.eventBus.EventBus
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.mapNotNull
import javax.inject.Inject

/**
 * Manager used to push appbar changes into [EventBus] to show on UI.
 *
 * @author Vladyslav Podrezenko
 */
class AppbarManager @Inject constructor(private val eventBus: EventBus) {
    /**
     * Same as [EventBus.bus] but mapped to [AppbarCommand].
     */
    val commands = eventBus.bus.mapNotNull { it as? AppbarCommand }.distinctUntilChanged()

    /**
     * Pushes [command][AppbarCommand] into [EventBus].
     *
     * @param command command to push into [EventBus]
     */
    fun manage(command: AppbarCommand) {
        eventBus.push(command)
    }
}