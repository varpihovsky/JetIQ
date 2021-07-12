package com.varpihovsky.core.exceptions

import kotlinx.coroutines.flow.MutableStateFlow

interface ViewModelWithException {
    val exceptions: MutableStateFlow<Exception?>

    fun onExceptionProcessed() {
        exceptions.value = null
    }
}