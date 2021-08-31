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
package com.varpihovsky.feature_profile

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.RouterState
import com.arkivanov.decompose.extensions.compose.jetbrains.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.arkivanov.decompose.value.Value
import com.varpihovsky.core_lifecycle.composition.LocalCompositionState
import com.varpihovsky.core_lifecycle.composition.Mode
import com.varpihovsky.feature_profile.profile.Profile
import com.varpihovsky.feature_profile.subjects.markbook.MarkbookSubjectScreen
import com.varpihovsky.feature_profile.subjects.success.SuccessSubjectScreen

const val SINGLE_PANE_WEIGHT = 1f
const val PROFILE_PANE_WEIGHT = 0.75f
const val DETAILS_PANE_WEIGHT = 0.25f

@Composable
fun ProfileScreen(profileRootComponent: ProfileRootComponent) {
    val isMultiPane by profileRootComponent.isMultiPane.subscribeAsState()

    profileRootComponent.drawerController.clear()

    Box {
        val isMultiPaneRequired = LocalCompositionState.current.currentMode == Mode.Desktop
        val routerState by profileRootComponent.detailsRouterState.subscribeAsState()

        LaunchedEffect(isMultiPaneRequired) {
            profileRootComponent.setMultiPane(isMultiPaneRequired)
        }

        Row(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier.weight(
                    if (isMultiPane && isRouterShown(routerState)) PROFILE_PANE_WEIGHT
                    else SINGLE_PANE_WEIGHT
                )
            ) {
                if (isMultiPane || !isRouterShown(routerState)) {
                    Profile(profileRootComponent.profileComponent)
                }
            }

            if (isMultiPane && isRouterShown(routerState)) {
                Box(modifier = Modifier.weight(DETAILS_PANE_WEIGHT))
            }
        }

        Row(modifier = Modifier.fillMaxSize()) {
            if (isMultiPane) {
                Box(modifier = Modifier.weight(PROFILE_PANE_WEIGHT))
            }

            Box(
                modifier = Modifier
                    .weight(if (isMultiPane) DETAILS_PANE_WEIGHT else SINGLE_PANE_WEIGHT)
                    .padding(LocalCompositionState.current.paddingValues)
            ) {
                DetailsPane(profileRootComponent.detailsRouterState)
            }
        }
    }
}

@Composable
private fun isRouterShown(routerState: RouterState<*, ProfileRootComponent.DetailsChild>) =
    routerState.activeChild.configuration !is ProfileDetailsRouter.Config.None

@Composable
private fun DetailsPane(routerState: Value<RouterState<*, ProfileRootComponent.DetailsChild>>) {
    Children(routerState) {
        when (val child = it.instance) {
            is ProfileRootComponent.DetailsChild.None -> Box {}
            is ProfileRootComponent.DetailsChild.Markbook -> MarkbookSubjectScreen(child.component)
            is ProfileRootComponent.DetailsChild.Success -> SuccessSubjectScreen(child.component)
        }
    }
}