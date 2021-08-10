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
package com.varpihovsky.feature_contacts

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.ExperimentalComposeUiApi
import com.varpihovsky.core.di.getViewModel
import com.varpihovsky.core.util.Selectable
import com.varpihovsky.core.util.selectedOnly
import com.varpihovsky.core_ui.compose.entities.ContactList
import com.varpihovsky.core_ui.compose.widgets.BackIconButton
import com.varpihovsky.feature_contacts.addition.AdditionDialog
import com.varpihovsky.ui_data.dto.UIReceiverDTO

@ExperimentalAnimationApi
@ExperimentalComposeUiApi
@ExperimentalFoundationApi
@Composable
fun ContactsScreen(
    contactsViewModel: ContactsViewModel
) {
    val contacts = contactsViewModel.data.contacts.collectAsState(listOf()).value

    ContactsAppbar(
        contactsViewModel = contactsViewModel,
        isChoosing = contactsViewModel.data.isChoosing.value,
        isExternalChoosing = contactsViewModel.data.isExternalChoosing.value,
        contacts = contacts
    )

    if (contactsViewModel.data.isAdding.value) {
        AdditionDialog(
            contactAdditionViewModel = getViewModel(),
            onDismissRequest = contactsViewModel::onDismissRequest,
            onConfirmButtonClick = contactsViewModel::onConfirmButtonClick
        )
    }

    ContactList(
        searchFieldValue = contactsViewModel.data.searchFieldValue.collectAsState().value,
        onSearchFieldValueChange = contactsViewModel::onSearchFieldValueChange,
        contacts = contacts,
        isLongClickEnabled = contactsViewModel.data.isLongClickEnabled.value,
        onLongClick = contactsViewModel::onContactLongClick,
        isClickEnabled = contactsViewModel.data.isClickEnabled.value,
        onClick = contactsViewModel::onContactClick
    )
}

@Composable
fun ContactsAppbar(
    contactsViewModel: ContactsViewModel,
    isChoosing: Boolean,
    isExternalChoosing: Boolean,
    contacts: List<Selectable<UIReceiverDTO>>
) {
    val text = if (isChoosing) "Вибір контактів..." else "Контакти"
    contactsViewModel.assignAppbar(
        title = text,
        icon = { BackIconButton(onClick = contactsViewModel::onBackNavButtonClick) },
        actions = {
            IconButton(onClick = contactsViewModel::onAddClick) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Додати")
            }
            if (!isExternalChoosing) {
                IconButton(
                    onClick = contactsViewModel::onRemoveClick,
                    enabled = contacts.selectedOnly().isNotEmpty()
                ) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = "Видалити")
                }
            }
        }
    )
}