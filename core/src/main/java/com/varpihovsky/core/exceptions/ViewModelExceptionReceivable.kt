package com.varpihovsky.core.exceptions

import android.util.Log

interface ViewModelExceptionReceivable : ViewModelWithException {
    fun send(e: Exception) {
        Log.d("ViewModels", Log.getStackTraceString(e))
        exceptions.value = e
    }
}