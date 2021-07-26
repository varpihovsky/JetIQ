package com.varpihovsky.jetiq.screens.settings.general

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
import com.varpihovsky.jetiq.screens.settings.SettingsSwitch
import com.varpihovsky.jetiq.ui.compose.BackIconButton
import com.varpihovsky.jetiq.ui.compose.ExposedDropDownList
import com.varpihovsky.jetiq.ui.compose.MapLifecycle
import com.varpihovsky.ui_data.DropDownItem

@Composable
fun GeneralSettingsScreen(generalSettingsViewModel: GeneralSettingsViewModel) {
    generalSettingsViewModel.assignAppbar(
        title = "Загальне",
        icon = { BackIconButton(generalSettingsViewModel::onBackNavButtonClick) }
    )

    MapLifecycle(viewModel = generalSettingsViewModel)

    BackHandler(onBack = generalSettingsViewModel::onBackNavButtonClick)

    val showNotifications by generalSettingsViewModel.showNotifications.collectAsState(initial = true)
    val successListType by generalSettingsViewModel.successListType.collectAsState(initial = DropDownItem.Empty)
    val markbookListType by generalSettingsViewModel.markbookListType.collectAsState(initial = DropDownItem.Empty)
    val buttonLocation by generalSettingsViewModel.buttonLocation.collectAsState(initial = DropDownItem.Empty)

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
        SettingsSwitch(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 10.dp, end = 10.dp, top = 5.dp, bottom = 5.dp),
            text = "Нотифікації про нові повідомлення.",
            checked = showNotifications,
            onCheckedChange = generalSettingsViewModel::onShowNotificationsSwitched
        )

        Divider(Modifier.fillMaxWidth())

        Text(
            modifier = Modifier.padding(start = 10.dp, top = 5.dp, bottom = 5.dp, end = 5.dp),
            text = "Списки",
            style = MaterialTheme.typography.h5
        )
        SubscribedExposedDropDownList(
            modifier = Modifier.padding(start = 10.dp, end = 10.dp, top = 5.dp, bottom = 5.dp),
            text = "Відображення списку журналу успішності.",
            suggestions = generalSettingsViewModel.listTypeSuggestions,
            selected = successListType,
            onSelect = { generalSettingsViewModel.onSuccessListTypeChange(it as DropDownItem.Simple) }
        )
        SubscribedExposedDropDownList(
            modifier = Modifier.padding(start = 10.dp, end = 10.dp, top = 5.dp, bottom = 5.dp),
            text = "Відображення списку залікової книжки.",
            suggestions = generalSettingsViewModel.listTypeSuggestions,
            selected = markbookListType,
            onSelect = { generalSettingsViewModel.onMarkbookListTypeChange(it as DropDownItem.Simple) }
        )
        SubscribedExposedDropDownList(
            modifier = Modifier.padding(start = 10.dp, end = 10.dp, top = 5.dp, bottom = 5.dp),
            text = "Місцеположення кнопки розширення в списках у профілі.",
            suggestions = generalSettingsViewModel.buttonLocationSuggestions,
            selected = buttonLocation,
            onSelect = { generalSettingsViewModel.onButtonLocationChange(it as DropDownItem.Simple) }
        )
        Divider(Modifier.fillMaxWidth())
    }
}

@Composable
private fun SubscribedExposedDropDownList(
    modifier: Modifier = Modifier,
    text: String,
    suggestions: List<DropDownItem>,
    selected: DropDownItem,
    onSelect: (DropDownItem) -> Unit
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            modifier = Modifier.padding(bottom = 5.dp),
            text = text,
            style = MaterialTheme.typography.h6
        )
        ExposedDropDownList(
            suggestions = suggestions,
            selected = selected,
            onSelect = onSelect
        )
    }
}