package com.varpihovsky.core_lifecycle

import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable

interface AppBarController {
    fun setText(text: String)

    fun setIconToDrawer()

    fun setIconToBack()

    fun hideIcon()

    fun setActions(actions: @Composable RowScope.() -> Unit)

    fun emptyActions()

    fun show()

    fun hide()
}