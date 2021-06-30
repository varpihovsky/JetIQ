package com.varpihovsky.jetiq.system.navigation

import androidx.navigation.compose.NamedNavArgument

interface NavigationCommand {
    val arguments: List<NamedNavArgument>
    val destination: String
    val type: NavigationCommandType
}

enum class NavigationCommandType {
    MAIN,
    SUBMENU,
    BACK
}