package com.varpihovsky.jetiq.system.exceptions

import kotlinx.coroutines.flow.MutableStateFlow

interface ViewModelWithException {
    val exceptions: MutableStateFlow<Exception?>

    fun onExceptionProcessed() {
        exceptions.value = null
    }
}