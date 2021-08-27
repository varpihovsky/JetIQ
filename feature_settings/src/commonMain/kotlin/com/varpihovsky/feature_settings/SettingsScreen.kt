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
package com.varpihovsky.feature_settings

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.RouterState
import com.arkivanov.decompose.extensions.compose.jetbrains.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.arkivanov.decompose.value.Value
import com.varpihovsky.core_lifecycle.composition.LocalCompositionState
import com.varpihovsky.core_lifecycle.composition.Mode
import com.varpihovsky.feature_settings.about.AboutSettingsScreen
import com.varpihovsky.feature_settings.general.GeneralSettingsScreen
import com.varpihovsky.feature_settings.main.MainSettingsScreen

private const val MULTIPANE_WIDTH_THRESHOLD = 800
private const val MAIN_PANE_WEIGH = 0.35f
private const val DETAILS_PANE_WEIGHT = 0.6f
private const val SINGLE_PANE_WEIGHT = 1f

@Composable
fun SettingsScreen(settingsRootComponent: SettingsRootComponent) {
    val isMultiPane by settingsRootComponent.isMultiPane.subscribeAsState()

    settingsRootComponent.appBarController.run {
        show()
        setIconToBack()
        setText("Налаштування")
        setActions { }
    }
    if (LocalCompositionState.current.currentMode is Mode.Mobile) {
        settingsRootComponent.bottomBarController.hide()
    }

    BoxWithConstraints {
        val isMultiPaneRequired = maxWidth >= MULTIPANE_WIDTH_THRESHOLD.dp

        LaunchedEffect(isMultiPaneRequired) {
            settingsRootComponent.setMultiPane(isMultiPaneRequired)
        }

        Row(modifier = Modifier.fillMaxSize()) {
            Box(modifier = Modifier.weight(if (isMultiPane) MAIN_PANE_WEIGH else SINGLE_PANE_WEIGHT)) {
                MainPane(settingsRootComponent.mainRouterState)
            }

            if (isMultiPane) {
                Box(modifier = Modifier.weight(DETAILS_PANE_WEIGHT))
            }
        }

        Row(modifier = Modifier.fillMaxSize()) {
            if (isMultiPane) {
                Box(modifier = Modifier.weight(MAIN_PANE_WEIGH))
            }

            Box(modifier = Modifier.weight(if (isMultiPane) DETAILS_PANE_WEIGHT else SINGLE_PANE_WEIGHT)) {
                DetailsPane(settingsRootComponent.detailsRouterState)
            }
        }
    }
}

@Composable
private fun MainPane(routerState: Value<RouterState<*, SettingsRootComponent.MainChild>>) {
    Children(routerState = routerState) {
        when (val child = it.instance) {
            is SettingsRootComponent.MainChild.None -> Box {}
            is SettingsRootComponent.MainChild.Main -> MainSettingsScreen(child.component)
        }
    }
}

@Composable
private fun DetailsPane(routerState: Value<RouterState<*, SettingsRootComponent.DetailsChild>>) {
    Children(routerState = routerState) {
        when (val child = it.instance) {
            is SettingsRootComponent.DetailsChild.None -> Box {}
            is SettingsRootComponent.DetailsChild.About -> AboutSettingsScreen(child.component)
            is SettingsRootComponent.DetailsChild.General -> GeneralSettingsScreen(child.component)
        }

    }
}