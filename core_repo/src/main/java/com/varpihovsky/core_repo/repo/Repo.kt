package com.varpihovsky.core_repo.repo

import com.varpihovsky.core.util.ThreadSafeMutableState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

abstract class Repo {
    protected val modelScope = CoroutineScope(Dispatchers.IO)

    private var prefix: String? = null

    internal fun getDebugPrefix(model: Repo): String? {
        if (prefix == null) {
            prefix = model::class.simpleName
        }
        return prefix
    }

    protected fun <T> mutableStateOf(value: T) = ThreadSafeMutableState(value, modelScope)
}