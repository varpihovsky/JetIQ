package com.varpihovsky.core.navigation

object NavigationDirections {
    val empty = object : NavigationCommand {
        override val destination = ""
    }

    val back = object : NavigationCommand {
        override val destination = ""
    }

    val authentication = object : NavigationCommand {
        override val destination = "auth"
    }

    val profile = object : NavigationCommand {
        override val destination = "profile"
    }

    val messages = object : NavigationCommand {
        override val destination = "messages"
    }

    val contacts = object : NavigationCommand {
        override val destination = "contacts"
    }

    val newMessage = object : NavigationCommand {
        override val destination = "new_message"
    }

    val mainSettings = object : NavigationCommand {
        override val destination = "settings_main"
    }

    val aboutSettings = object : NavigationCommand {
        override val destination = "settings_about"
    }
}