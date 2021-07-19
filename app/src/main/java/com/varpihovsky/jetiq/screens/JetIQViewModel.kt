package com.varpihovsky.jetiq.screens

import android.util.Log
import androidx.annotation.CallSuper
import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.varpihovsky.core.appbar.AppbarCommand
import com.varpihovsky.core.appbar.AppbarManager
import com.varpihovsky.core.exceptions.ExceptionEventManager
import com.varpihovsky.core.util.ThreadSafeMutableState
import com.varpihovsky.core_nav.main.NavigationController

abstract class JetIQViewModel(
    private val appbarManager: AppbarManager,
    private val navigationController: NavigationController,
    private val exceptionEventManager: ExceptionEventManager
) : ViewModel() {
    fun assignAppbar(
        title: String? = null,
        icon: (@Composable () -> Unit)? = null,
        actions: (@Composable RowScope.() -> Unit)? = null
    ) {
        appbarManager.manage(
            AppbarCommand.Configured(
                title = title,
                navIcon = icon,
                actions = actions
            )
        )
    }

    fun assignAppbar(bar: @Composable () -> Unit) {
        appbarManager.manage(AppbarCommand.Custom(bar))
    }

    fun emptyAppbar() {
        appbarManager.manage(AppbarCommand.Empty)
    }

    @CallSuper
    open fun onBackNavButtonClick() {
        navigationController.onBack()
    }

    open fun onCompose() {

    }

    open fun onDispose() {

    }

    protected fun redirectExceptionToUI(exception: Exception) {
        Log.d("ViewModels", Log.getStackTraceString(exception))
        exceptionEventManager.pushException(exception)
    }

    fun <T> mutableStateOf(t: T): ThreadSafeMutableState<T> {
        return ThreadSafeMutableState(t, viewModelScope)
    }
}