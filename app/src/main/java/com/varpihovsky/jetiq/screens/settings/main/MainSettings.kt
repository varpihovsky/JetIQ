package com.varpihovsky.jetiq.screens.settings.main

/* JetIQ
 * Copyright © 2021 Vladyslav Podrezenko
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.varpihovsky.jetiq.R
import com.varpihovsky.jetiq.screens.settings.SettingsButton
import com.varpihovsky.jetiq.ui.compose.BackIconButton

@Composable
fun MainSettingsScreen(
    mainSettingsViewModel: MainSettingsViewModel
) {
    mainSettingsViewModel.assignAppbar(
        title = "Налаштування",
        icon = { BackIconButton(mainSettingsViewModel::onBackNavButtonClick) },
    )

    BackHandler(true, onBack = mainSettingsViewModel::onBackNavButtonClick)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        SettingsButton(
            title = "Загальне",
            hint = "",
            onClick = mainSettingsViewModel::onGeneralClick,
            icon = painterResource(id = R.drawable.ic_baseline_settings_applications_24)
        )
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