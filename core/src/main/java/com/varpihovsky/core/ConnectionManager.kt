package com.varpihovsky.core

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import java.net.InetAddress

class ConnectionManager {
    fun isConnected(host: String = DEFAULT_HOST) = runBlocking {
        GlobalScope.async(Dispatchers.IO) {
            try {
                InetAddress.getByName(host)
                true
            } catch (e: Exception) {
                false
            }
        }.await()
    }


    companion object {
        const val DEFAULT_HOST = "jetiq.vntu.edu.ua"
    }
}