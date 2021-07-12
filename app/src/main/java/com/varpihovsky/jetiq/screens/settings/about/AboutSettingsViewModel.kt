package com.varpihovsky.jetiq.screens.settings.about

import com.varpihovsky.core.navigation.NavigationManager
import com.varpihovsky.jetiq.appbar.AppbarManager
import com.varpihovsky.jetiq.screens.JetIQViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AboutSettingsViewModel @Inject constructor(
    appbarManager: AppbarManager,
    navigationManager: NavigationManager,
) : JetIQViewModel(appbarManager, navigationManager)