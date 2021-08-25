package com.varpihovsky.core.util

expect object JetIQPlatformTools {
    val currentPlatform: Platform
}

sealed class Platform {
    object Android : Platform()
    object JVM : Platform()
}