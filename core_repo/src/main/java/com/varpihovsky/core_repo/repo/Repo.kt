package com.varpihovsky.core_repo.repo

import com.varpihovsky.core.exceptions.ExceptionEventManager
import com.varpihovsky.core.util.ThreadSafeMutableState
import com.varpihovsky.core_network.result.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

/**
 * Standard class for all Repo classes. Controls exception behaviour. Has useful methods for
 * [Result] processing.
 *
 * @author Vladyslav Podrezenko
 */
abstract class Repo(val exceptionEventManager: ExceptionEventManager) {

    /**
     * Coroutine scope created specially for repo automatically.
     */
    protected val modelScope = CoroutineScope(Dispatchers.IO)

    /**
     * Doesn't process any case of [Result]. Only pushes exception event into event bus if there was
     * any error.
     *
     * @param result result got from any manager from [com.varpihovsky.core_network.managers] package.
     *
     * @return [Unit]
     */
    protected fun <T> wrapException(result: Result<T>) =
        wrapException(result = result, onSuccess = {})

    /**
     * Wraps [Result] class to process only onSuccess case. If there was error exception event pushed
     * into event bus.
     *
     * @param result result got from any manager from [com.varpihovsky.core_network.managers] package.
     * @param onSuccess lambda which will be invoked if result was successful.
     *
     * @return [Unit]
     */
    protected inline fun <T> wrapException(
        result: Result<T>,
        onSuccess: (Result.Success<T>) -> Unit
    ) = wrapException(
        result = result,
        onSuccess = onSuccess,
        onFailure = {}
    )

    /**
     * Wraps [Result] class to process all cases of result. If result is success invokes onSuccess
     * lambda. If result is empty invokes onFailure lambda. If result is failure pushes exception into
     * event bus and invokes onFailure lambda.
     *
     * Due to invoking onFailure lambda in empty result case this method is not useful in use with
     * [EmptyResult].
     *
     * @param result result got from any manager from [com.varpihovsky.core_network.managers] package.
     * @param onSuccess lambda which will be invoked if result was successful.
     * @param onFailure lambda which will be invoked if result was failed.
     *
     * @return R generic.
     */
    protected inline fun <T, R> wrapException(
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

    /**
     * Returns [ThreadSafeMutableState] which is safe to change not in main dispatcher.
     *
     * @param value default value
     *
     * @return [ThreadSafeMutableState]
     */
    protected fun <T> mutableStateOf(value: T) = ThreadSafeMutableState(value, modelScope)
}