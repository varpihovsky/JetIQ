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
import androidx.compose.runtime.Composable
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue
import soup.compose.material.motion.MotionSpec

/**
 * Class that stores data about entry.
 *
 * @author Vladyslav Podrezenko
 */
@Parcelize
class NavigationEntry(
    /**
     * Specifies composable of entry. On one controller can be shown only one composable.
     */
    val composable: @Composable () -> Unit,

    /**
     * Specifies route of entry.
     */
    val route: String,

    /**
     * Specifies type of entry.
     *
     * @see EntryType
     */
    val type: @RawValue EntryType,

    /**
     * Specifies animation that will be played when composable appears.
     */
    val inAnimation: @RawValue MotionSpec,

    /**
     * Specifies animation that will be played when composable removed from stack.
     */
    val outAnimation: @RawValue MotionSpec
) : Parcelable

