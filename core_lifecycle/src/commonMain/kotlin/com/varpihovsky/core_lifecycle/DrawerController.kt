package com.varpihovsky.core_lifecycle

import androidx.compose.runtime.Composable

interface DrawerController {
    fun setNavigation(text: String, content: @Composable () -> Unit)

    fun clear()

    fun hide()
}