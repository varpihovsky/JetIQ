package com.varpihovsky.jetiq.system.navigation

import androidx.navigation.compose.NamedNavArgument

object NavigationDirections {
    val empty = object : NavigationCommand {
        override val arguments = emptyList<NamedNavArgument>()
        override val destination = ""
    }

    val authentication = object : NavigationCommand {
        override val arguments = emptyList<NamedNavArgument>()
        override val destination = "auth"
    }

    val profile = object : NavigationCommand {
        override val arguments = emptyList<NamedNavArgument>()
        override val destination = "profile"
    }

    val messages = object : NavigationCommand {
        override val arguments = emptyList<NamedNavArgument>()
        override val destination = "messages"
    }
}