package com.varpihovsky.core_lifecycle

import androidx.compose.runtime.Composable

interface DrawerController {
    fun setNavigation(content: @Composable () -> Unit, text: String)

    fun clear()
}