package com.varpihovsky.core.eventBus

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull

/**
 * Class used to have centralized flow for events. Subscribers will be invoked when [bus] value is changed
 * but doesn't remembers any history of changes.
 *
 * @author Vladyslav Podrezenko
 */
class EventBus {
    val bus: Flow<Any>
        get() = _bus.filterNotNull()

    private val _bus = MutableStateFlow<Any?>(null)

    /**
     * Pushes event into [bus].
     *
     * @param event event to push.
     */
    fun push(event: Any) {
        _bus.value = event
    }
}