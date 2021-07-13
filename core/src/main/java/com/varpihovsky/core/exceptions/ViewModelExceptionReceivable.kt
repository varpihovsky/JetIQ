package com.varpihovsky.core.exceptions

import android.util.Log

interface ViewModelExceptionReceivable : ViewModelWithException {
    fun send(e: Throwable) {
        Log.d("ViewModels", Log.getStackTraceString(e))
        exceptions.value = e
    }
}