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
package com.varpihovsky.feature_contacts.addition

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.varpihovsky.core.util.CoroutineDispatchers
import com.varpihovsky.core.util.Selectable
import com.varpihovsky.core.util.SelectionEngine
import com.varpihovsky.core.util.selectedOnly
import com.varpihovsky.core_lifecycle.JetIQComponentContext
import com.varpihovsky.core_repo.repo.ListRepo
import com.varpihovsky.core_repo.repo.ProfileRepo
import com.varpihovsky.ui_data.dto.ContactTypeDropDownItem
import com.varpihovsky.ui_data.dto.DropDownItem
import com.varpihovsky.ui_data.dto.ReceiverType
import com.varpihovsky.ui_data.dto.UIReceiverDTO
import com.varpihovsky.ui_data.mappers.toUIDTO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class ContactAdditionComponent(
    jetiqComponentContext: JetIQComponentContext
) : JetIQComponentContext by jetiqComponentContext, KoinComponent {
    lateinit var callback: (List<UIReceiverDTO>) -> Unit

    val selectedContactType: Value<ContactTypeDropDownItem> by lazy { _selectedContactType }
    val faculties: Value<List<DropDownItem.WithID>> by lazy { _faculties }
    val selectedFaculty: Value<DropDownItem.WithID> by lazy { _selectedFaculty }
    val groups: Value<List<DropDownItem.WithID>> by lazy { _groups }
    val selectedGroup: Value<DropDownItem.WithID> by lazy { _selectedGroup }
    val searchFieldValue: Value<String> by lazy { _searchFieldValue }

    val contacts: Flow<List<Selectable<UIReceiverDTO>>>
        get() = listRepo.getContacts()
            .map { contacts -> contacts.map { it.toUIDTO() } }
            .combine(selectionEngine.state) { contacts, receivers ->
                receivers.sortedBy { it.dto.text }.filter { receiver ->
                    contacts.find { it.id == receiver.dto.id && it.type == receiver.dto.type } == null
                }
            }


    private val dispatchers: CoroutineDispatchers by inject()
    private val listRepo: ListRepo by inject()
    private val profileRepo: ProfileRepo by inject()

    private val scope = CoroutineScope(Dispatchers.Main)

    private val _selectedContactType = MutableValue<ContactTypeDropDownItem>(ContactTypeDropDownItem.STUDENT)
    private val _faculties = MutableValue(listOf<DropDownItem.WithID>())
    private val _selectedFaculty = MutableValue(DropDownItem.WithID())
    private val _groups = MutableValue<List<DropDownItem.WithID>>(listOf())
    private val _selectedGroup = MutableValue(DropDownItem.WithID())
    private val _searchFieldValue = MutableValue("")

    private val contactsIncomingState = MutableStateFlow(listOf<UIReceiverDTO>())
    private val selectionEngine = SelectionEngine(contactsIncomingState, scope, dispatchers.Default)

    init {
        scope.launch(dispatchers.IO) {
            _faculties.value = listRepo.getFaculties().map { DropDownItem.WithID(it.id, it.name) }
        }
    }

    fun onDismiss() {
        clearFields()
        _selectedContactType.value = ContactTypeDropDownItem.STUDENT
    }

    fun onConfirm() {
        callback(selectionEngine.state.value.selectedOnly())
        clearFields()
    }

    fun onContactTypeSelected(type: ContactTypeDropDownItem) {
        if (_selectedContactType.value != type) {
            _selectedContactType.value = type
            clearFields()
        }
    }

    fun onFacultySelect(faculty: DropDownItem) {
        _selectedFaculty.value = faculty as DropDownItem.WithID
        scope.launch(dispatchers.IO) {
            _groups.value =
                listRepo.getGroupByFaculty(faculty.id).map { DropDownItem.WithID(it.id, it.name) }
        }
    }

    fun onGroupSelect(group: DropDownItem) {
        _selectedGroup.value = group as DropDownItem.WithID
        scope.launch(dispatchers.IO) {
            val id = profileRepo.getProfileDTO()?.id?.toInt()

            contactsIncomingState.value = listRepo.getStudentsByGroup(group.id)
                .map { UIReceiverDTO(it.id, it.fullName, ReceiverType.STUDENT) }
                .filter { it.id != id }
        }
    }

    fun onSearchFieldValueChange(value: String) {
        _searchFieldValue.value = value
        scope.launch(dispatchers.IO) {
            contactsIncomingState.value = listRepo.getTeacherByQuery(value)
                .map { UIReceiverDTO(it.id, it.fullName, ReceiverType.TEACHER) }
        }
    }

    fun onContactSelected(contact: Selectable<UIReceiverDTO>) {
        selectionEngine.toggle(contact)
    }

    private fun clearFields() {
        contactsIncomingState.value = listOf()
        _searchFieldValue.value = ""
        _selectedGroup.value = DropDownItem.WithID()
        _groups.value = listOf()
        _selectedFaculty.value = DropDownItem.WithID()
    }
}