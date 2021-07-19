package com.varpihovsky.core.eventBus

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class EventBus {
    private val _bus = MutableStateFlow<Any?>(null)
    val bus: Flow<Any?> = _bus

    fun push(event: Any) {
        _bus.value = event
    }
}