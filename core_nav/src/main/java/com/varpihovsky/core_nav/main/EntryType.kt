package com.varpihovsky.core_nav.main

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

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Type of current entry. From entry type depends behavior of [NavigationController.manage] command.
 * When entry is Main it deletes current navigation stack and pushes it to the top.
 * Wen entry is SubMenu it pushes it to the top of the stack without deleting.
 *
 * @see [NavigationController]
 *
 * @author Vladyslav Podrezenko
 */
sealed class EntryType {
    @Parcelize
    object Main : EntryType(), Parcelable

    @Parcelize
    object SubMenu : EntryType(), Parcelable
}