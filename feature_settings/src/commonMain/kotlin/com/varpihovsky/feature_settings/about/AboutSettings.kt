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
package com.varpihovsky.feature_settings.about

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.varpihovsky.core_lifecycle.composition.LocalCompositionState
import com.varpihovsky.core_lifecycle.composition.Mode
import com.varpihovsky.core_ui.compose.OpenPage
import com.varpihovsky.core_ui.compose.widgets.FullWidthButton

@Composable
internal fun AboutSettingsScreen(aboutSettingsViewModel: AboutSettingsComponent) {
    if (LocalCompositionState.current.currentMode is Mode.Mobile) {
        aboutSettingsViewModel.appBarController.run {
            show()
            setText("Про додаток")
            setIconToBack()
        }
    }

    val pageToOpen by aboutSettingsViewModel.pageToOpen.subscribeAsState()

    if (pageToOpen.isNotEmpty()) {
        OpenPage(url = pageToOpen)
        aboutSettingsViewModel.onPageOpened()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "JetIQ",
            style = MaterialTheme.typography.h1,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colors.onBackground
        )
        Text(
            modifier = Modifier
                .padding(horizontal = 40.dp, vertical = 10.dp)
                .fillMaxWidth(),
            text = "Неофіційний клієнт для JetIQ v1.1.1-alpha.",
            style = MaterialTheme.typography.h5,
            textAlign = TextAlign.Center
        )
        FullWidthButton(
            title = "Розробник",
            hint = "Подрезенко Владислав",
            onClick = {}
        )
        FullWidthButton(
            title = "GitHub",
            hint = "github.com/varpihovsky",
            onClick = { aboutSettingsViewModel.onUrlButtonClick("https://www.github.com/varpihovsky") }
        )
        FullWidthButton(
            title = "LinkedIn",
            hint = "linkedin.com/in/varpihovsky",
            onClick = { aboutSettingsViewModel.onUrlButtonClick("https://www.linkedin.com/in/varpihovsky") }
        )
    }
}