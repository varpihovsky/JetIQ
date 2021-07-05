package com.varpihovsky.jetiq.system.navigation

import androidx.navigation.compose.NamedNavArgument

object NavigationDirections {
    val empty = object : NavigationCommand {
        override val arguments = emptyList<NamedNavArgument>()
        override val destination = ""
        override val type = NavigationCommandType.MAIN
    }

    val back = object : NavigationCommand {
        override val arguments = emptyList<NamedNavArgument>()
        override val destination = ""
        override val type = NavigationCommandType.BACK
    }

    val authentication = object : NavigationCommand {
        override val arguments = emptyList<NamedNavArgument>()
        override val destination = "auth"
        override val type = NavigationCommandType.MAIN
    }

    val profile = object : NavigationCommand {
        override val arguments = emptyList<NamedNavArgument>()
        override val destination = "profile"
        override val type = NavigationCommandType.MAIN
    }

    val messages = object : NavigationCommand {
        override val arguments = emptyList<NamedNavArgument>()
        override val destination = "messages"
        override val type = NavigationCommandType.MAIN
    }

    val contacts = object : NavigationCommand {
        override val arguments = emptyList<NamedNavArgument>()
        override val destination = "contacts"
        override val type = NavigationCommandType.SUBMENU
    }

    val newMessage = object : NavigationCommand {
        override val arguments = emptyList<NamedNavArgument>()
        override val destination = "new_message"
        override val type = NavigationCommandType.SUBMENU
    }

    val mainSettings = object : NavigationCommand {
        override val arguments = emptyList<NamedNavArgument>()
        override val destination = "settings_main"
        override val type = NavigationCommandType.SUBMENU
    }

    val aboutSettings = object : NavigationCommand {
        override val arguments = emptyList<NamedNavArgument>()
        override val destination = "settings_about"
        override val type = NavigationCommandType.SUBMENU
    }
}