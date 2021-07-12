package com.varpihovsky.core.util

import android.util.Log

fun logException(e: RuntimeException, tag: String = "Application") {
    Log.d(tag, Log.getStackTraceString(e))
}