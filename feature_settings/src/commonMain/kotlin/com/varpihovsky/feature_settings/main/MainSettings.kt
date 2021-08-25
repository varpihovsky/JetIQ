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
package com.varpihovsky.feature_settings.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.SettingsApplications
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.varpihovsky.core_ui.compose.widgets.FullWidthButton

@Composable
internal fun MainSettingsScreen(mainSettingsComponent: MainSettingsComponent) {
    mainSettingsComponent.appBarController.run {
        show()
        setText("Налаштування")
        setIconToBack()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        FullWidthButton(
            title = "Загальне",
            hint = "",
            onClick = mainSettingsComponent::onGeneralClick,
            icon = {
                Icon(
                    imageVector = Icons.Default.SettingsApplications,
                    contentDescription = null
                )
            }
        )
        FullWidthButton(
            title = "Про додаток",
            hint = "",
            onClick = mainSettingsComponent::onAboutClick,
            icon = {
                Icon(
                    imageVector = Icons.Default.Help,
                    contentDescription = null
                )
            }
        )
        FullWidthButton(
            title = "Вихід",
            hint = "Вийти з облікового запису,\nБАЗА ДАНИХ БУДЕ СТЕРТА!",
            onClick = mainSettingsComponent::onLogoutClick,
            icon = {
                Icon(
                    imageVector = Icons.Default.Logout,
                    contentDescription = null
                )
            }
        )
    }
}