package com.varpihovsky.jetiq.back.model

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

abstract class Model {
    protected val modelScope = CoroutineScope(Dispatchers.IO)

    private var prefix: String? = null

    fun getDebugPrefix(model: Model): String? {
        if (prefix == null) {
            prefix = model::class.simpleName
        }
        return prefix
    }
}