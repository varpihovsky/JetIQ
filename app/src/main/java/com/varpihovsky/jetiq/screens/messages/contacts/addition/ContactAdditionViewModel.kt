package com.varpihovsky.jetiq.screens.messages.contacts.addition

import androidx.compose.runtime.State
import androidx.lifecycle.viewModelScope
import com.varpihovsky.core.appbar.AppbarManager
import com.varpihovsky.core.exceptions.ExceptionEventManager
import com.varpihovsky.core.util.CoroutineDispatchers
import com.varpihovsky.core.util.Selectable
import com.varpihovsky.core.util.replaceAndReturn
import com.varpihovsky.core_nav.main.NavigationController
import com.varpihovsky.core_repo.repo.ListRepo
import com.varpihovsky.jetiq.screens.JetIQViewModel
import com.varpihovsky.ui_data.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContactAdditionViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val listRepo: ListRepo,
    appbarManager: AppbarManager,
    navigationController: NavigationController,
    exceptionEventManager: ExceptionEventManager
) : JetIQViewModel(appbarManager, navigationController, exceptionEventManager) {
    val data by lazy { Data() }
    lateinit var callback: (List<UIReceiverDTO>) -> Unit

    private val selectedContactType = mutableStateOf(ContactTypeDropDownItem.STUDENT)
    private val faculties = mutableStateOf(listOf<IdDropDownItem>())
    private val selectedFaculty = mutableStateOf(IdDropDownItem())
    private val groups = mutableStateOf<List<IdDropDownItem>>(listOf())
    private val selectedGroup = mutableStateOf(IdDropDownItem())
    private val searchFieldValue = mutableStateOf("")

    //TODO: Replace with selection engine.
    private val contacts = mutableStateOf(listOf<Selectable<UIReceiverDTO>>())

    inner class Data {
        val selectedContactType: State<ContactTypeDropDownItem> =
            this@ContactAdditionViewModel.selectedContactType
        val faculties: State<List<IdDropDownItem>> = this@ContactAdditionViewModel.faculties
        val selectedFaculty: State<IdDropDownItem> =
            this@ContactAdditionViewModel.selectedFaculty
        val groups: State<List<IdDropDownItem>> = this@ContactAdditionViewModel.groups
        val selectedGroup: State<IdDropDownItem> = this@ContactAdditionViewModel.selectedGroup
        val searchFieldValue: State<String> = this@ContactAdditionViewModel.searchFieldValue
        val contacts: State<List<Selectable<UIReceiverDTO>>> =
            this@ContactAdditionViewModel.contacts
    }

    init {
        viewModelScope.launch(dispatchers.IO) {
            faculties.value = listRepo.getFaculties().map { IdDropDownItem(it.id, it.text) }
        }
    }

    fun onDismiss() {
        selectedContactType.value = ContactTypeDropDownItem.STUDENT
        clearFields()
    }

    fun onConfirm() {
        callback(contacts.value.filter { it.isSelected }.map { it.dto })
        clearFields()
    }

    fun onContactTypeSelected(type: ContactTypeDropDownItem) {
        if (selectedContactType.value != type) {
            selectedContactType.value = type
            clearFields()
        }
    }

    fun onFacultySelect(faculty: DropDownItem) {
        selectedFaculty.value = faculty as IdDropDownItem
        viewModelScope.launch(dispatchers.IO) {
            groups.value =
                listRepo.getGroupByFaculty(faculty.id).map { IdDropDownItem(it.id, it.text) }
        }
    }

    fun onGroupSelect(group: DropDownItem) {
        selectedGroup.value = group as IdDropDownItem
        viewModelScope.launch(dispatchers.IO) {
            contacts.value = listRepo.getStudentsByGroup(group.id)
                .map { Selectable(UIReceiverDTO(it.id, it.text, ReceiverType.STUDENT), false) }
        }
    }

    fun onSearchFieldValueChange(value: String) {
        searchFieldValue.value = value
        viewModelScope.launch(dispatchers.IO) {
            contacts.value = listRepo.getTeacherByQuery(value).map {
                Selectable(UIReceiverDTO(it.id, it.text, ReceiverType.TEACHER), false)
            }
        }
    }

    fun onContactSelected(contact: Selectable<UIReceiverDTO>) {
        val current = contacts.value
        contacts.value = current.replaceAndReturn(contact, contact.selectedToggle())
    }

    private fun clearFields() {
        contacts.value = listOf()
        searchFieldValue.value = ""
        selectedGroup.value = IdDropDownItem()
        groups.value = listOf()
        selectedFaculty.value = IdDropDownItem()
    }
}