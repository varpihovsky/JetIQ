package com.varpihovsky.jetiq.screens.settings.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.varpihovsky.jetiq.R
import com.varpihovsky.jetiq.screens.settings.SettingsButton
import com.varpihovsky.jetiq.ui.compose.BasicAppBar
import com.varpihovsky.jetiq.ui.compose.MapLifecycle

@Composable
fun MainSettingsScreen(
    mainSettingsViewModel: MainSettingsViewModel
) {
    mainSettingsViewModel.assignAppbar {
        BasicAppBar(
            title = "Налаштування",
            onBackClick = mainSettingsViewModel::onBackNavButtonClick
        )
    }

    MapLifecycle(viewModel = mainSettingsViewModel)

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