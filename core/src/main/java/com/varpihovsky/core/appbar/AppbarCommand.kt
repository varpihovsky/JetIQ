package com.varpihovsky.core.appbar

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

import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable

/**
 * Class used for specify current look of Appbar.
 *
 * @author Vladyslav Podrezenko
 */
sealed class AppbarCommand {
    /**
     * Specifies current appbar look as custom one.
     *
     * @author Vladyslav Podrezenko
     */
    class Custom(val bar: @Composable () -> Unit) : AppbarCommand()

    /**
     * Removes appbar from the screen.
     *
     * @author Vladyslav Podrezenko
     */
    object Empty : AppbarCommand()

    /**
     * Specifies only slots of the appbar in the default material design way.
     *
     * @author Vladyslav Podrezenko
     */
    class Configured(
        val title: String?,
        val navIcon: (@Composable () -> Unit)?,
        val actions: (@Composable RowScope.() -> Unit)?
    ) : AppbarCommand()
}