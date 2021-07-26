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

import androidx.annotation.DrawableRes
import com.varpihovsky.core.R

sealed class BottomNavigationItem(
    val route: NavigationCommand,
    val subscription: String,
    @DrawableRes val iconId: Int
) {
    object ProfileItem : BottomNavigationItem(
        NavigationDirections.profile,
        "Профіль",
        R.drawable.ic_baseline_person_24
    )

    object MessagesItem : BottomNavigationItem(
        NavigationDirections.messages,
        "Повідомлення",
        R.drawable.ic_baseline_message_24
    )
}