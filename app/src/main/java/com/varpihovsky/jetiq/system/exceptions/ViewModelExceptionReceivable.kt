package com.varpihovsky.jetiq.system.exceptions

interface ViewModelExceptionReceivable : ViewModelWithException {
    fun send(e: Exception) {
        exceptions.value = e
    }
}