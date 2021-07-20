package com.varpihovsky.core.appbar

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