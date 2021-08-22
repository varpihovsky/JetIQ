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
package com.varpihovsky.feature_messages.contacts.addition

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.varpihovsky.core.util.Selectable
import com.varpihovsky.core_ui.compose.entities.Contact
import com.varpihovsky.core_ui.compose.foundation.AlertDialog
import com.varpihovsky.core_ui.compose.widgets.BasicTextButton
import com.varpihovsky.core_ui.compose.widgets.HorizontalSubscribedExposedDropDownList
import com.varpihovsky.core_ui.compose.widgets.SearchBar
import com.varpihovsky.ui_data.dto.ContactTypeDropDownItem
import com.varpihovsky.ui_data.dto.DropDownItem
import com.varpihovsky.ui_data.dto.UIReceiverDTO
import soup.compose.material.motion.MaterialSharedAxisX

@ExperimentalAnimationApi
@ExperimentalComposeUiApi
@ExperimentalFoundationApi
@Composable
fun AdditionDialog(
    contactAdditionComponent: ContactAdditionComponent,
    onDismissRequest: () -> Unit,
    onConfirmButtonClick: (List<UIReceiverDTO>) -> Unit
) {
    LaunchedEffect(onConfirmButtonClick) {
        contactAdditionComponent.callback = onConfirmButtonClick
    }

    val selectedContactType by contactAdditionComponent.selectedContactType.subscribeAsState()
    val faculties by contactAdditionComponent.faculties.subscribeAsState()
    val selectedFaculty by contactAdditionComponent.selectedFaculty.subscribeAsState()
    val groups by contactAdditionComponent.groups.subscribeAsState()
    val selectedGroup by contactAdditionComponent.selectedGroup.subscribeAsState()
    val searchFieldValue by contactAdditionComponent.searchFieldValue.subscribeAsState()
    val contacts by contactAdditionComponent.contacts.collectAsState(initial = listOf())

    AdditionDialog(
        onDismissRequest = {
            onDismissRequest()
            contactAdditionComponent.onDismiss()
        },
        onConfirmButtonClick = contactAdditionComponent::onConfirm,
        selectedContactType = selectedContactType,
        onContactTypeSelected = { contactAdditionComponent.onContactTypeSelected(it) },
        faculties = faculties,
        selectedFaculty = selectedFaculty,
        onFacultySelect = contactAdditionComponent::onFacultySelect,
        groups = groups,
        selectedGroup = selectedGroup,
        onGroupSelect = contactAdditionComponent::onGroupSelect,
        searchFieldValue = searchFieldValue,
        onSearchFieldValueChange = contactAdditionComponent::onSearchFieldValueChange,
        contacts = contacts,
        onContactSelected = contactAdditionComponent::onContactSelected
    )
}

@ExperimentalAnimationApi
@ExperimentalComposeUiApi
@ExperimentalFoundationApi
@Composable
private fun AdditionDialog(
    onDismissRequest: () -> Unit,
    onConfirmButtonClick: () -> Unit,
    selectedContactType: ContactTypeDropDownItem,
    onContactTypeSelected: (ContactTypeDropDownItem) -> Unit,
    faculties: List<DropDownItem>,
    selectedFaculty: DropDownItem,
    onFacultySelect: (DropDownItem) -> Unit,
    groups: List<DropDownItem>,
    selectedGroup: DropDownItem,
    onGroupSelect: (DropDownItem) -> Unit,
    searchFieldValue: String,
    onSearchFieldValueChange: (String) -> Unit,
    contacts: List<Selectable<UIReceiverDTO>>,
    onContactSelected: (Selectable<UIReceiverDTO>) -> Unit
) {
    AlertDialog(
        modifier = Modifier
            .wrapContentSize()
            .animateContentSize()
            .padding(20.dp),
        onDismissRequest = onDismissRequest,
        confirmButton = {
            BasicTextButton(
                onClick = onConfirmButtonClick,
                text = "Додати"
            )
        },
        dismissButton = {
            BasicTextButton(
                onClick = onDismissRequest,
                text = "Відхилити"
            )
        },
        title = { Text(modifier = Modifier.padding(10.dp), text = "Новий контакт") },
        text = {
            Column {
                HorizontalSubscribedExposedDropDownList(
                    modifier = Modifier.padding(bottom = 7.dp),
                    text = "Тип: ",
                    suggestions = listOf(
                        ContactTypeDropDownItem.TEACHER,
                        ContactTypeDropDownItem.STUDENT
                    ),
                    selected = selectedContactType,
                    onSelect = { onContactTypeSelected(it as ContactTypeDropDownItem) }
                )

                Divider(modifier = Modifier.fillMaxWidth())

                MaterialSharedAxisX(
                    targetState = selectedContactType,
                    forward = true
                ) {
                    when (it) {
                        ContactTypeDropDownItem.STUDENT -> @Composable {
                            Column {
                                HorizontalSubscribedExposedDropDownList(
                                    modifier = Modifier.padding(top = 7.dp, bottom = 7.dp),
                                    text = "Факультет/Інститут: ",
                                    suggestions = faculties,
                                    selected = selectedFaculty,
                                    onSelect = onFacultySelect
                                )

                                HorizontalSubscribedExposedDropDownList(
                                    modifier = Modifier.padding(top = 7.dp, bottom = 7.dp),
                                    text = "Група: ",
                                    suggestions = groups,
                                    selected = selectedGroup,
                                    onSelect = onGroupSelect
                                )
                            }
                        }
                        ContactTypeDropDownItem.TEACHER -> @Composable {
                            SearchBar(
                                searchFieldValue = searchFieldValue,
                                onSearchFieldValueChange = onSearchFieldValueChange
                            )
                        }
                    }
                }

                LazyColumn(modifier = Modifier.heightIn(min = 0.dp, max = 300.dp)) {
                    items(count = contacts.size, key = { contacts[it].dto }) {
                        Contact(
                            contact = contacts[it],
                            onLongClick = { },
                            onClick = onContactSelected
                        )
                    }
                }
            }
        }
    )
}