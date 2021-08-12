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
package com.varpihovsky.feature_settings.general

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.varpihovsky.core_lifecycle.assignAppbar
import com.varpihovsky.core_ui.compose.widgets.BackIconButton
import com.varpihovsky.repo_data.UserPreferences
import com.varpihovsky.ui_data.dto.DropDownItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

@Composable
fun GeneralSettingsScreen(generalSettingsViewModel: GeneralSettingsViewModel) {
    generalSettingsViewModel.assignAppbar(
        title = "Загальне",
        icon = { BackIconButton(generalSettingsViewModel::onBackNavButtonClick) }
    )

    GeneralSettings(generalSettingsViewModel)
}

@Composable
internal expect fun GeneralSettings(generalSettingsViewModel: GeneralSettingsViewModel)

@Composable
internal fun <T> Flow<UserPreferences>.preferenceFlowToState(
    selector: UserPreferences.() -> T
) = map { DropDownItem.Simple(selector(it).toString()) }
    .distinctUntilChanged()
    .collectAsState(DropDownItem.Empty)
