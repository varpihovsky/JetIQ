package com.varpihovsky.core.exceptions

import com.varpihovsky.core.eventBus.EventBus
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.mapNotNull
import javax.inject.Inject

class ExceptionEventManager @Inject constructor(private val eventBus: EventBus) {
    val exceptions = eventBus.bus.mapNotNull { it as? Throwable }.distinctUntilChanged()

    fun pushException(exception: Throwable) {
        eventBus.push(exception)
    }
}