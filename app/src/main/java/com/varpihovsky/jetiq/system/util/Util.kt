package com.varpihovsky.jetiq.system.util

import android.util.Log

fun logException(e: RuntimeException) {
    Log.d("Application", Log.getStackTraceString(e))
}