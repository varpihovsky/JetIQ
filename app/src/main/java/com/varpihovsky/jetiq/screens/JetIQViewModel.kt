package com.varpihovsky.jetiq.screens

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.varpihovsky.core.exceptions.ViewModelWithException
import com.varpihovsky.core.navigation.NavigationDirections
import com.varpihovsky.core.navigation.NavigationManager
import com.varpihovsky.core.util.ThreadSafeMutableState
import com.varpihovsky.jetiq.appbar.AppbarCommand
import com.varpihovsky.jetiq.appbar.AppbarManager
import kotlinx.coroutines.flow.MutableStateFlow

abstract class JetIQViewModel(
    private val appbarManager: AppbarManager,
    private val navigationManager: NavigationManager,
) : ViewModel(), ViewModelWithException {
    override val exceptions: MutableStateFlow<Throwable?> = MutableStateFlow(null)

    fun assignAppbar(bar: @Composable () -> Unit) {
        appbarManager.manage(AppbarCommand(bar))
    }

    fun emptyAppbar() {
        appbarManager.manage(AppbarCommand { })
    }

    open fun onBackNavButtonClick() {
        navigationManager.manage(NavigationDirections.back)
    }

    open fun onCompose() {

    }

    open fun onDispose() {

    }

    protected fun redirectExceptionToUI(exception: Exception) {
        Log.d("ViewModels", Log.getStackTraceString(exception))
        exceptions.value = exception
    }

    fun <T> mutableStateOf(t: T): ThreadSafeMutableState<T> {
        return ThreadSafeMutableState(t, viewModelScope)
    }
}