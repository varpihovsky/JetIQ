package com.varpihovsky.jetiq.back.api

import com.varpihovsky.jetiq.system.ConnectionManager
import com.varpihovsky.jetiq.system.exceptions.ResponseUnsuccessfulException
import retrofit2.Response

abstract class JetIQManager constructor(
    private val connectionManager: ConnectionManager
) {
    fun throwExceptionWhenNotConnected() {
//        if (!connectionManager.isConnected()) {
//            throw ResponseUnsuccessfulException("Немає підключення до інтернету")
//        }
    }

    fun <T> throwExceptionWhenUnsuccessful(
        response: Response<T>,
        message: String,
        predicate: T.() -> Boolean = { false },
    ) {
        if (!response.isSuccessful || response.body() == null || predicate(response.body()!!)) {
            throw ResponseUnsuccessfulException(message)
        }
    }

    fun throwExceptionWhenNull(message: String, vararg any: Any?) {
        if (any.mapNotNull { it }.size < any.size) {
            throw ResponseUnsuccessfulException(message)
        }
    }
}