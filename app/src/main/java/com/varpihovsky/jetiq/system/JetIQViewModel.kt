package com.varpihovsky.jetiq.system

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.varpihovsky.jetiq.system.exceptions.ViewModelWithException
import com.varpihovsky.jetiq.system.navigation.NavigationDirections
import com.varpihovsky.jetiq.system.navigation.NavigationManager
import com.varpihovsky.jetiq.system.util.ThreadSafeMutableState
import com.varpihovsky.jetiq.ui.appbar.AppbarCommand
import com.varpihovsky.jetiq.ui.appbar.AppbarManager
import kotlinx.coroutines.flow.MutableStateFlow

abstract class JetIQViewModel(
    private val appbarManager: AppbarManager,
    private val navigationManager: NavigationManager,
) : ViewModel(), ViewModelWithException {
    override val exceptions: MutableStateFlow<Exception?> = MutableStateFlow(null)

    fun assignAppbar(bar: @Composable () -> Unit) {
        appbarManager.manage(AppbarCommand(bar))
    }

    fun emptyAppbar() {
        appbarManager.manage(AppbarCommand { })
    }

    open fun onBackNavButtonClick() {
        navigationManager.manage(NavigationDirections.back)
    }

    protected fun redirectExceptionToUI(exception: Exception) {
        Log.d("ViewModels", Log.getStackTraceString(exception))
        exceptions.value = exception
    }

    fun <T> mutableStateOf(t: T): ThreadSafeMutableState<T> {
        return ThreadSafeMutableState(t, viewModelScope)
    }
}