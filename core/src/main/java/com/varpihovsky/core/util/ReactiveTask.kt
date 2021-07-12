package com.varpihovsky.core.util

import kotlinx.coroutines.*

class ReactiveTask(
    dispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val task: suspend () -> Unit
) {
    private var asyncTask: Job? = null
    private val scope = CoroutineScope(dispatcher)

    fun start() {
        if (asyncTask?.isActive == true) {
            stop()
        }

        asyncTask = scope.async {
            task()
        }
    }

    fun stop() {
        asyncTask?.cancel()
    }
}