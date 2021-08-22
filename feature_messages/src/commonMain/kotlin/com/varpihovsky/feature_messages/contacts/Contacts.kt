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
package com.varpihovsky.feature_messages.contacts

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.varpihovsky.core.util.Selectable
import com.varpihovsky.core.util.selectedOnly
import com.varpihovsky.core_ui.compose.entities.ContactList
import com.varpihovsky.feature_messages.contacts.addition.AdditionDialog
import com.varpihovsky.ui_data.dto.UIReceiverDTO

@OptIn(ExperimentalFoundationApi::class, ExperimentalComposeUiApi::class, ExperimentalAnimationApi::class)
@Composable
fun ContactsScreen(
    contactsComponent: ContactsComponent,
    isMultiPane: Boolean
) {
    val contacts by contactsComponent.contacts.collectAsState(listOf())
    val isChoosing by contactsComponent.isChoosing.subscribeAsState()
    val isExternalChoosing by contactsComponent.isExternalChoosing.subscribeAsState()
    val isAdding by contactsComponent.isAdding.subscribeAsState()
    val searchFieldValue by contactsComponent.searchFieldValue.collectAsState()

    if (!isMultiPane) {
        ContactsAppbar(
            contactsComponent = contactsComponent,
            isChoosing = isChoosing,
            isExternalChoosing = isExternalChoosing,
            contacts = contacts
        )
    }

    if (isAdding) {
        AdditionDialog(
            contactAdditionComponent = contactsComponent.contactsAdditionComponent,
            onDismissRequest = contactsComponent::onDismissRequest,
            onConfirmButtonClick = contactsComponent::onConfirmButtonClick
        )
    }

    ContactList(
        searchFieldValue = searchFieldValue,
        onSearchFieldValueChange = contactsComponent::onSearchFieldValueChange,
        contacts = contacts,
        onLongClick = contactsComponent::onContactLongClick,
        onClick = contactsComponent::onContactClick
    )
}

@Composable
fun ContactsAppbar(
    contactsComponent: ContactsComponent,
    isChoosing: Boolean,
    isExternalChoosing: Boolean,
    contacts: List<Selectable<UIReceiverDTO>>
) {
    val text = if (isChoosing) "Вибір контактів..." else "Контакти"
    contactsComponent.appBarController.run {
        show()
        setText(text)
        setIconToBack()
        setActions {
            IconButton(onClick = contactsComponent::onAddClick) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Додати")
            }
            if (!isExternalChoosing) {
                IconButton(
                    onClick = contactsComponent::onRemoveClick,
                    enabled = contacts.selectedOnly().isNotEmpty()
                ) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = "Видалити")
                }
            }
        }
    }
}