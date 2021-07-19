package com.varpihovsky.core_repo.repo

import com.varpihovsky.core.exceptions.ExceptionEventManager
import com.varpihovsky.core.util.ThreadSafeMutableState
import com.varpihovsky.core_network.result.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

abstract class Repo(val exceptionEventManager: ExceptionEventManager) {

    protected val modelScope = CoroutineScope(Dispatchers.IO)

    private var prefix: String? = null

    internal fun getDebugPrefix(model: Repo): String? {
        if (prefix == null) {
            prefix = model::class.simpleName
        }
        return prefix
    }

    inline fun <T> wrapException(result: Result<T>) = wrapException(result = result, onSuccess = {})

    inline fun <T> wrapException(
        result: Result<T>,
        onSuccess: (Result.Success<T>) -> Unit
    ) = wrapException(
        result = result,
        onSuccess = onSuccess,
        onFailure = {}
    )

    inline fun <T, R> wrapException(
        result: Result<T>,
        onSuccess: (Result.Success<T>) -> R,
        onFailure: () -> R
    ) = when {
        result.isSuccess() -> {
            onSuccess(result.asSuccess())
        }
        result.isEmpty() -> {
            onFailure()
        }
        else -> {
            result.asFailure().error?.let { exceptionEventManager.pushException(it) }
            onFailure()
        }
    }

    protected fun <T> mutableStateOf(value: T) = ThreadSafeMutableState(value, modelScope)
}