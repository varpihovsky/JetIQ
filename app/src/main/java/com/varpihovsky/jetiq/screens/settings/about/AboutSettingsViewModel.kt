package com.varpihovsky.jetiq.screens.settings.about

import androidx.compose.runtime.State
import com.varpihovsky.core.appbar.AppbarManager
import com.varpihovsky.core.exceptions.ExceptionEventManager
import com.varpihovsky.core_nav.main.NavigationController
import com.varpihovsky.jetiq.screens.JetIQViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AboutSettingsViewModel @Inject constructor(
    appbarManager: AppbarManager,
    navigationController: NavigationController,
    exceptionEventManager: ExceptionEventManager,
) : JetIQViewModel(appbarManager, navigationController, exceptionEventManager) {
    val data by lazy { Data() }

    private val pageToOpen = mutableStateOf("")

    inner class Data {
        val pageToOpen: State<String> = this@AboutSettingsViewModel.pageToOpen
    }

    fun onUrlButtonClick(url: String) {
        pageToOpen.value = url
    }

    fun onPageOpened() {
        pageToOpen.value = ""
    }
}