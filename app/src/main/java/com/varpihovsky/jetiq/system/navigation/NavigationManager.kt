package com.varpihovsky.jetiq.system.navigation

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class NavigationManager {
    val commands: StateFlow<NavigationCommand> by lazy { _commands }

    private val _commands = MutableStateFlow(NavigationDirections.empty)

    fun navigate(
        command: NavigationCommand
    ){
        _commands.value = command
    }
}