package com.varpihovsky.jetiq.system

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

abstract class FlowManager<T>(defaultValue: T) {
    private val flowStack = mutableListOf<T>()

    open val commands: Flow<T> by lazy { _commands }

    private val _commands = flow<T> {
        while (true) {
            flowStack.forEach {
                emit(it)
            }
            flowStack.clear()
            delay(25)
        }
    }

    fun manage(command: T) {
        onCommandReceived(command)
        flowStack.add(command)
    }

    protected open fun onCommandReceived(command: T) {

    }
}