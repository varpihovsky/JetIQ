package com.varpihovsky.jetiq.system.util

import android.util.Log

fun logException(e: RuntimeException, tag: String = "Application") {
    Log.d(tag, Log.getStackTraceString(e))
}