package com.varpihovsky.core.exceptions

import com.varpihovsky.core.eventBus.EventBus
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.mapNotNull
import javax.inject.Inject

/**
 * Manager which pushes exception into [EventBus]. It will be shown on UI lately.
 *
 * @param eventBus needed to push exception.
 *
 * @author Vladyslav Podrezenko
 */
class ExceptionEventManager @Inject constructor(private val eventBus: EventBus) {
    /**
     * Same as [EventBus.bus] but with exceptions only.
     */
    val exceptions = eventBus.bus.mapNotNull { it as? Throwable }.distinctUntilChanged()

    /**
     * Pushes exception into [EventBus] to show exception on UI.
     *
     * @param exception exception to show on UI.
     */
    fun pushException(exception: Throwable) {
        eventBus.push(exception)
    }
}