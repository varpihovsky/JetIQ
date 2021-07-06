package com.varpihovsky.jetiq.screens.messages.contacts.addition

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.AlertDialog
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import com.varpihovsky.jetiq.screens.messages.contacts.Contact
import com.varpihovsky.jetiq.screens.messages.contacts.SearchBar
import com.varpihovsky.jetiq.ui.compose.BasicTextButton
import com.varpihovsky.jetiq.ui.compose.CollectExceptions
import com.varpihovsky.jetiq.ui.compose.SubscribedExposedDropDownList
import com.varpihovsky.jetiq.ui.dto.ContactTypeDropDownItem
import com.varpihovsky.jetiq.ui.dto.DropDownItem
import com.varpihovsky.jetiq.ui.dto.EmptyDropDownItem
import com.varpihovsky.jetiq.ui.dto.UIReceiverDTO
import com.varpihovsky.jetiq.ui.dto.func_extensions.Selectable
import soup.compose.material.motion.Axis
import soup.compose.material.motion.MaterialSharedAxis

@ExperimentalFoundationApi
@Composable
fun AdditionDialog(
    contactAdditionViewModel: ContactAdditionViewModel,
    onDismissRequest: () -> Unit,
    onConfirmButtonClick: (List<UIReceiverDTO>) -> Unit
) {
    contactAdditionViewModel.callback = onConfirmButtonClick

    val selectedContactType = contactAdditionViewModel.data.selectedContactType.observeAsState(
        initial = ContactTypeDropDownItem.STUDENT
    )
    val faculties = contactAdditionViewModel.data.faculties.observeAsState(initial = listOf())
    val selectedFaculty =
        contactAdditionViewModel.data.selectedFaculty.observeAsState(initial = EmptyDropDownItem())
    val groups = contactAdditionViewModel.data.groups.observeAsState(initial = listOf())
    val selectedGroup =
        contactAdditionViewModel.data.selectedGroup.observeAsState(initial = EmptyDropDownItem())
    val searchFieldValue =
        contactAdditionViewModel.data.searchFieldValue.observeAsState(initial = "")
    val contacts = contactAdditionViewModel.data.contacts.observeAsState(initial = listOf())

    CollectExceptions(viewModel = contactAdditionViewModel)

    AdditionDialog(
        onDismissRequest = {
            onDismissRequest()
            contactAdditionViewModel.onDismiss()
        },
        onConfirmButtonClick = contactAdditionViewModel::onConfirm,
        selectedContactType = selectedContactType.value,
        onContactTypeSelected = contactAdditionViewModel::onContactTypeSelected,
        faculties = faculties.value,
        selectedFaculty = selectedFaculty.value,
        onFacultySelect = contactAdditionViewModel::onFacultySelect,
        groups = groups.value,
        selectedGroup = selectedGroup.value,
        onGroupSelect = contactAdditionViewModel::onGroupSelect,
        searchFieldValue = searchFieldValue.value,
        onSearchFieldValueChange = contactAdditionViewModel::onSearchFieldValueChange,
        contacts = contacts.value,
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
                                    text = "Факультет/Інститут: ",
                                    suggestions = faculties,
                                    selected = selectedFaculty,
                                    onSelect = onFacultySelect
                                )

                                SubscribedExposedDropDownList(
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