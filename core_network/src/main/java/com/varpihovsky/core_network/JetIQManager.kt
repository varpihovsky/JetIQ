package com.varpihovsky.core_network

import com.varpihovsky.core_network.result.Result
import com.varpihovsky.core_network.result.asFailure
import com.varpihovsky.core_network.result.asSuccess
import com.varpihovsky.core_network.result.isSuccess

abstract class JetIQManager {
    protected fun <T, R> mapResult(
        result: Result<T>,
        unwrapBlock: (Result.Success<T>) -> Result<R>
    ): Result<R> = if (result.isSuccess()) {
        unwrapBlock(result.asSuccess())
    } else {
        result.asFailure()
    }
}