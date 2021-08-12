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

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.varpihovsky.core_repo.repo.PreferencesKeys
import com.varpihovsky.core_ui.compose.widgets.FullWidthSwitch
import com.varpihovsky.core_ui.compose.widgets.VerticalSubscribedExposedDropDownList
import com.varpihovsky.repo_data.ExpandButtonLocation
import com.varpihovsky.repo_data.SubjectListType
import com.varpihovsky.ui_data.dto.DropDownItem
import kotlinx.coroutines.flow.map

private val listTypeSuggestions =
    listOf(SubjectListType.PARTIAL, SubjectListType.FULL).map { DropDownItem.Simple(it.toString()) }

private val buttonLocationSuggestions = listOf(
    ExpandButtonLocation.LOWER,
    ExpandButtonLocation.UPPER
).map { DropDownItem.Simple(it.toString()) }

@Composable
internal actual fun GeneralSettings(generalSettingsViewModel: GeneralSettingsViewModel) {
    val showNotifications by generalSettingsViewModel.preferenceData.map { it.showNotifications }
        .collectAsState(initial = true)
    val successListType by generalSettingsViewModel.preferenceData.preferenceFlowToState { successListType }
    val markbookListType by generalSettingsViewModel.preferenceData.preferenceFlowToState { markbookListType }
    val buttonLocation by generalSettingsViewModel.preferenceData.preferenceFlowToState { profileListExpandButtonLocation }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            modifier = Modifier.padding(start = 10.dp, top = 5.dp, bottom = 5.dp, end = 5.dp),
            text = "Нотифікування",
            style = MaterialTheme.typography.h5
        )
        FullWidthSwitch(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 10.dp, end = 10.dp, top = 5.dp, bottom = 5.dp),
            text = "Нотифікації про нові повідомлення.",
            checked = showNotifications,
            onCheckedChange = {
                generalSettingsViewModel.onPreferenceSet(
                    PreferencesKeys.SHOW_NOTIFICATION,
                    PreferenceValueMapper.map(PreferencesKeys.SHOW_NOTIFICATION, it)
                )
            }
        )

        Divider(Modifier.fillMaxWidth())

        Text(
            modifier = Modifier.padding(start = 10.dp, top = 5.dp, bottom = 5.dp, end = 5.dp),
            text = "Списки",
            style = MaterialTheme.typography.h5
        )
        VerticalSubscribedExposedDropDownList(
            modifier = Modifier.padding(start = 10.dp, end = 10.dp, top = 5.dp, bottom = 5.dp),
            text = "Відображення списку журналу успішності.",
            suggestions = listTypeSuggestions,
            selected = successListType,
            onSelect = {
                generalSettingsViewModel.onPreferenceSet(
                    PreferencesKeys.SUCCESS_LIST_TYPE,
                    PreferenceValueMapper.map(
                        PreferencesKeys.SUCCESS_LIST_TYPE,
                        (it as DropDownItem.Simple).text
                    )
                )
            }
        )
        VerticalSubscribedExposedDropDownList(
            modifier = Modifier.padding(start = 10.dp, end = 10.dp, top = 5.dp, bottom = 5.dp),
            text = "Відображення списку залікової книжки.",
            suggestions = listTypeSuggestions,
            selected = markbookListType,
            onSelect = {
                generalSettingsViewModel.onPreferenceSet(
                    PreferencesKeys.MARKBOOK_LIST_TYPE,
                    PreferenceValueMapper.map(
                        PreferencesKeys.MARKBOOK_LIST_TYPE,
                        (it as DropDownItem.Simple).text
                    )
                )
            }
        )
        VerticalSubscribedExposedDropDownList(
            modifier = Modifier.padding(start = 10.dp, end = 10.dp, top = 5.dp, bottom = 5.dp),
            text = "Місцеположення кнопки розширення в списках у профілі.",
            suggestions = buttonLocationSuggestions,
            selected = buttonLocation,
            onSelect = {
                generalSettingsViewModel.onPreferenceSet(
                    PreferencesKeys.EXPAND_BUTTON_LOCATION,
                    PreferenceValueMapper.map(
                        PreferencesKeys.EXPAND_BUTTON_LOCATION,
                        (it as DropDownItem.Simple).text
                    )
                )
            }
        )
        Divider(Modifier.fillMaxWidth())
    }
}