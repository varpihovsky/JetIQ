package com.varpihovsky.core

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

abstract class FlowManager<T>(defaultValue: T) {
    private val flowStack = mutableListOf<T>()

    open val commands: Flow<T> by lazy { _commands }

    private val _commands = MutableStateFlow(defaultValue)

    fun manage(command: T) {
        onCommandReceived(command)
        _commands.value = command
    }

    protected open fun onCommandReceived(command: T) {

    }
}