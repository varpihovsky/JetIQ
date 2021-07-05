package com.varpihovsky.jetiq.system

import java.net.InetAddress

class ConnectionManager {
    fun isConnected(host: String = DEFAULT_HOST) =
        try {
            InetAddress.getByName(host)
            true
        } catch (e: Exception) {
            false
        }

    companion object {
        const val DEFAULT_HOST = "jetiq.vntu.edu.ua"
    }
}