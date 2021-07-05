package com.varpihovsky.jetiq.back.api

import com.varpihovsky.jetiq.system.ConnectionManager
import retrofit2.Response

abstract class JetIQManager constructor(
    private val connectionManager: ConnectionManager
) {
    fun throwExceptionWhenNotConnected() {
        if (!connectionManager.isConnected()) {
            throw RuntimeException("Немає підключення до інтернету")
        }
    }

    fun <T> throwExceptionWhenUnsuccessful(
        response: Response<T>,
        message: String,
        predicate: T.() -> Boolean = { false },
    ) {
        if (!response.isSuccessful || response.body() == null || predicate(response.body()!!)) {
            throw RuntimeException(message)
        }
    }

    fun throwExceptionWhenNull(message: String, vararg any: Any?) {
        if (any.mapNotNull { it }.size < any.size) {
            throw RuntimeException(message)
        }
    }

    fun <T> exceptionWrap(
        provider: () -> Response<T>
    ): T {
        throwExceptionWhenNotConnected()

        val response = provider()

        throwExceptionWhenUnsuccessful(response, STANDARD_ERROR_MESSAGE)

        return response.body()!!
    }

    companion object {
        @JvmStatic
        protected val STANDARD_ERROR_MESSAGE = "Критична помилка, зверніться до розробників"
    }
}