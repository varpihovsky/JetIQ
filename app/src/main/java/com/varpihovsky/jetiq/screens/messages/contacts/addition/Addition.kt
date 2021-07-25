package com.varpihovsky.jetiq.screens.messages.contacts.addition

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

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.AlertDialog
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.varpihovsky.core.util.Selectable
import com.varpihovsky.jetiq.screens.messages.contacts.Contact
import com.varpihovsky.jetiq.screens.messages.contacts.SearchBar
import com.varpihovsky.jetiq.ui.compose.BasicTextButton
import com.varpihovsky.jetiq.ui.compose.MapLifecycle
import com.varpihovsky.jetiq.ui.compose.SubscribedExposedDropDownList
import com.varpihovsky.ui_data.ContactTypeDropDownItem
import com.varpihovsky.ui_data.DropDownItem
import com.varpihovsky.ui_data.UIReceiverDTO
import soup.compose.material.motion.Axis
import soup.compose.material.motion.MaterialSharedAxis

@ExperimentalFoundationApi
@Composable
fun AdditionDialog(
    contactAdditionViewModel: ContactAdditionViewModel,
    onDismissRequest: () -> Unit,
    onConfirmButtonClick: (List<UIReceiverDTO>) -> Unit
) {
    MapLifecycle(viewModel = contactAdditionViewModel)

    contactAdditionViewModel.callback = onConfirmButtonClick

    AdditionDialog(
        onDismissRequest = {
            onDismissRequest()
            contactAdditionViewModel.onDismiss()
        },
        onConfirmButtonClick = contactAdditionViewModel::onConfirm,
        selectedContactType = contactAdditionViewModel.data.selectedContactType.value,
        onContactTypeSelected = { contactAdditionViewModel.onContactTypeSelected(it) },
        faculties = contactAdditionViewModel.data.faculties.value,
        selectedFaculty = contactAdditionViewModel.data.selectedFaculty.value,
        onFacultySelect = contactAdditionViewModel::onFacultySelect,
        groups = contactAdditionViewModel.data.groups.value,
        selectedGroup = contactAdditionViewModel.data.selectedGroup.value,
        onGroupSelect = contactAdditionViewModel::onGroupSelect,
        searchFieldValue = contactAdditionViewModel.data.searchFieldValue.value,
        onSearchFieldValueChange = contactAdditionViewModel::onSearchFieldValueChange,
        contacts = contactAdditionViewModel.data.contacts.value,
        onContactSelected = contactAdditionViewModel::onContactSelected
    )
}

@ExperimentalFoundationApi
@Composable
fun AdditionDialog(
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
        modifier = Modifier.padding(20.dp),
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
        title = { Text(text = "Новий контакт") },
        text = {
            Column {
                SubscribedExposedDropDownList(
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

                MaterialSharedAxis(
                    targetState = selectedContactType,
                    axis = Axis.X,
                    forward = true
                ) {
                    when (it) {
                        ContactTypeDropDownItem.STUDENT -> @Composable {
                            Column {
                                SubscribedExposedDropDownList(
                                    modifier = Modifier.padding(top = 7.dp, bottom = 7.dp),
                                    text = "Факультет/Інститут: ",
                                    suggestions = faculties,
                                    selected = selectedFaculty,
                                    onSelect = onFacultySelect
                                )

                                SubscribedExposedDropDownList(
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

                LazyColumn {
                    items(count = contacts.size, key = { contacts[it] }) {
                        Contact(
                            contact = contacts[it],
                            isLongClickEnabled = false,
                            onLongClick = { },
                            isClickEnabled = true,
                            onClick = onContactSelected
                        )
                    }
                }
            }
        }
    )
}