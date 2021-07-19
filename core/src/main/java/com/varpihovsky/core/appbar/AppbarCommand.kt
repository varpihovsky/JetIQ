package com.varpihovsky.core.appbar

import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable

sealed class AppbarCommand {
    class Custom(val bar: @Composable () -> Unit) : AppbarCommand()

    object Empty : AppbarCommand()

    class Configured(
        val title: String?,
        val navIcon: (@Composable () -> Unit)?,
        val actions: (@Composable RowScope.() -> Unit)?
    ) : AppbarCommand()
}