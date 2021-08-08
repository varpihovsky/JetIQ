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

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import soup.compose.material.motion.EnterMotionSpec
import soup.compose.material.motion.ExitMotionSpec

/**
 * Class that stores data about entry.
 *
 * @author Vladyslav Podrezenko
 */
@OptIn(ExperimentalAnimationApi::class)
class NavigationEntry(
    /**
     * Specifies composable of entry. On one controller can be shown only one composable.
     * padding values are passed by [DisplayNavigation][com.varpihovsky.core_nav.dsl.DisplayNavigation]
     * function. If [applyPaddingValues] were set to true can be ignored.
     */
    var composable: @Composable (paddingValues: PaddingValues) -> Unit,
    /**
     * Specifies route of entry.
     */
    val route: String,

    /**
     * Specifies type of entry.
     *
     * @see EntryType
     */
    val type: EntryType,

    /**
     * Specifies animation that will be played when composable appears.
     */
    val inAnimation: EnterMotionSpec,

    /**
     * Specifies animation that will be played when composable removed from stack.
     */
    val outAnimation: ExitMotionSpec,

    /**
     * Specifies if [DisplayNavigation][com.varpihovsky.core_nav.dsl.DisplayNavigation] should apply
     * padding values by yourself.
     */
    val applyPaddingValues: Boolean
) {
    override fun toString(): String {
        return "NavigationEntry(" +
                "\n\tcomposable=$composable," +
                "\n\troute='$route'," +
                "\n\ttype=$type," +
                "\n\tinAnimation=$inAnimation," +
                "\n\toutAnimation=$outAnimation," +
                "\n\tapplyPaddingValues=$applyPaddingValues\n" +
                ")"
    }
}

