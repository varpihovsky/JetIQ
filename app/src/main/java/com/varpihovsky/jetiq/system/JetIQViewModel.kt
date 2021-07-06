package com.varpihovsky.jetiq.system

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import com.varpihovsky.jetiq.system.navigation.NavigationDirections
import com.varpihovsky.jetiq.system.navigation.NavigationManager
import com.varpihovsky.jetiq.ui.appbar.AppbarCommand
import com.varpihovsky.jetiq.ui.appbar.AppbarManager

abstract class JetIQViewModel(
    private val appbarManager: AppbarManager,
    private val navigationManager: NavigationManager,
) : ViewModel() {
    fun assignAppbar(bar: @Composable () -> Unit) {
        appbarManager.manage(AppbarCommand(bar))
    }

    fun emptyAppbar() {
        appbarManager.manage(AppbarCommand { })
    }

    open fun onBackNavButtonClick() {
        navigationManager.manage(NavigationDirections.back)
    }

}