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

import androidx.compose.runtime.State
import androidx.lifecycle.viewModelScope
import com.varpihovsky.core.appbar.AppbarManager
import com.varpihovsky.core.exceptions.ExceptionEventManager
import com.varpihovsky.core.util.*
import com.varpihovsky.core_nav.main.NavigationController
import com.varpihovsky.core_repo.repo.ListRepo
import com.varpihovsky.core_repo.repo.ProfileRepo
import com.varpihovsky.jetiq.screens.JetIQViewModel
import com.varpihovsky.ui_data.*
import com.varpihovsky.ui_data.mappers.toUIDTO
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContactAdditionViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val listRepo: ListRepo,
    private val profileRepo: ProfileRepo,
    appbarManager: AppbarManager,
    navigationController: NavigationController,
    exceptionEventManager: ExceptionEventManager
) : JetIQViewModel(appbarManager, navigationController, exceptionEventManager) {
    val data by lazy { Data() }
    lateinit var callback: (List<UIReceiverDTO>) -> Unit

    private val selectedContactType =
        mutableStateOf<ContactTypeDropDownItem>(ContactTypeDropDownItem.STUDENT)
    private val faculties = mutableStateOf(listOf<DropDownItem.WithID>())
    private val selectedFaculty = mutableStateOf(DropDownItem.WithID())
    private val groups = mutableStateOf<List<DropDownItem.WithID>>(listOf())
    private val selectedGroup = mutableStateOf(DropDownItem.WithID())
    private val searchFieldValue = mutableStateOf("")

    private val contactsState = MutableStateFlow(listOf<UIReceiverDTO>())
    private val selectionEngine =
        SelectionEngine(contactsState, viewModelScope, dispatchers.Default)

    inner class Data {
        val selectedContactType: State<ContactTypeDropDownItem> =
            this@ContactAdditionViewModel.selectedContactType
        val faculties: State<List<DropDownItem.WithID>> = this@ContactAdditionViewModel.faculties
        val selectedFaculty: State<DropDownItem.WithID> =
            this@ContactAdditionViewModel.selectedFaculty
        val groups: State<List<DropDownItem.WithID>> = this@ContactAdditionViewModel.groups
        val selectedGroup: State<DropDownItem.WithID> = this@ContactAdditionViewModel.selectedGroup
        val searchFieldValue: State<String> = this@ContactAdditionViewModel.searchFieldValue
        val contacts: Flow<List<Selectable<UIReceiverDTO>>> = listRepo.getContacts()
            .map { contacts -> contacts.map { it.toUIDTO() } }
            .combine(selectionEngine.state) { contacts, receivers ->
                receivers.sortedBy { it.dto.text }.filter { receiver ->
                    contacts.find { it.id == receiver.dto.id && it.type == receiver.dto.type } == null
                }
            }
    }

    init {
        viewModelScope.launch(dispatchers.IO) {
            faculties.value = listRepo.getFaculties().map { DropDownItem.WithID(it.id, it.text) }
        }
    }

    fun onDismiss() {
        clearFields()
        selectedContactType.value = ContactTypeDropDownItem.STUDENT
    }

    fun onConfirm() {
        callback(selectionEngine.state.value.selectedOnly())
        clearFields()
    }

    fun onContactTypeSelected(type: ContactTypeDropDownItem) {
        if (selectedContactType.value != type) {
            selectedContactType.value = type
            clearFields()
        }
    }

    fun onFacultySelect(faculty: DropDownItem) {
        selectedFaculty.value = faculty as DropDownItem.WithID
        viewModelScope.launch(dispatchers.IO) {
            groups.value =
                listRepo.getGroupByFaculty(faculty.id).map { DropDownItem.WithID(it.id, it.text) }
        }
    }

    fun onGroupSelect(group: DropDownItem) {
        selectedGroup.value = group as DropDownItem.WithID
        viewModelScope.launch(dispatchers.IO) {
            val id = profileRepo.getProfileDTO().id.toInt()

            contactsState.value = listRepo.getStudentsByGroup(group.id)
                .map { UIReceiverDTO(it.id, it.text, ReceiverType.STUDENT) }
                .filter { it.id != id }
        }
    }

    fun onSearchFieldValueChange(value: String) {
        searchFieldValue.value = value
        viewModelScope.launch(dispatchers.IO) {
            contactsState.value = listRepo.getTeacherByQuery(value)
                .map { UIReceiverDTO(it.id, it.text, ReceiverType.TEACHER) }
        }
    }

    fun onContactSelected(contact: Selectable<UIReceiverDTO>) {
        selectionEngine.toggle(contact)
    }

    private fun clearFields() {
        contactsState.value = listOf()
        searchFieldValue.value = ""
        selectedGroup.value = DropDownItem.WithID()
        groups.value = listOf()
        selectedFaculty.value = DropDownItem.WithID()
    }
}