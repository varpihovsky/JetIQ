package com.varpihovsky.core_nav.navigation

/* JetIQ
 * Copyright © 2021 Vladyslav Podrezenko
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

object NavigationDirections {
    val empty = object : NavigationCommand {
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

    val generalSettings = object : NavigationCommand {
        override val destination = "settings_general"
    }

    val aboutSettings = object : NavigationCommand {
        override val destination = "settings_about"
    }
}