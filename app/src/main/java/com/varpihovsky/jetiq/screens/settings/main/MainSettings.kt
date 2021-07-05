package com.varpihovsky.jetiq.screens.settings.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.varpihovsky.jetiq.R
import com.varpihovsky.jetiq.screens.settings.SettingsAppBar
import com.varpihovsky.jetiq.screens.settings.SettingsButton

@Composable
fun MainSettingsScreen(
    mainSettingsViewModel: MainSettingsViewModel
) {
    mainSettingsViewModel.assignAppbar {
        SettingsAppBar(title = "Налаштування", onBackClick = mainSettingsViewModel::onBackClick)
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        SettingsButton(
            title = "Про додаток",
            hint = "",
            onClick = mainSettingsViewModel::onAboutClick,
            icon = painterResource(id = R.drawable.ic_baseline_help_24)
        )
        SettingsButton(
            title = "Вихід",
            hint = "Вийти з облікового запису,\nБАЗА ДАНИХ БУДЕ СТЕРТА!",
            onClick = mainSettingsViewModel::onLogoutClick,
            icon = painterResource(id = R.drawable.ic_baseline_logout_24)
        )
    }
}