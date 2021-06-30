package com.varpihovsky.jetiq.screens.settings.about

import com.varpihovsky.jetiq.system.JetIQViewModel
import com.varpihovsky.jetiq.system.navigation.NavigationManager
import com.varpihovsky.jetiq.ui.appbar.AppbarManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AboutSettingsViewModel @Inject constructor(
    appbarManager: AppbarManager,
    navigationManager: NavigationManager
) : JetIQViewModel(appbarManager, navigationManager)