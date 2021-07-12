package com.varpihovsky.jetiq.screens.messages.contacts.addition

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.varpihovsky.core.exceptions.ViewModelWithException
import com.varpihovsky.core_repo.repo.ListRepo
import com.varpihovsky.ui_data.*
import com.varpihovsky.ui_data.func_extensions.Selectable
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContactAdditionViewModel @Inject constructor(
    private val listModel: ListRepo
) : ViewModel(), ViewModelWithException {
    val data by lazy { Data() }
    lateinit var callback: (List<UIReceiverDTO>) -> Unit
    override val exceptions: MutableStateFlow<Exception?> = MutableStateFlow(null)

    private val selectedContactType = MutableLiveData(ContactTypeDropDownItem.STUDENT)
    private val faculties = MutableLiveData<List<IdDropDownItem>>()
    private val selectedFaculty = MutableLiveData<IdDropDownItem>()
    private val groups = MutableLiveData<List<IdDropDownItem>>()
    private val selectedGroup = MutableLiveData<IdDropDownItem>()
    private val searchFieldValue = MutableLiveData("")
    private val contacts = MutableLiveData<List<Selectable<UIReceiverDTO>>>()

    inner class Data {
        val selectedContactType: LiveData<ContactTypeDropDownItem> =
            this@ContactAdditionViewModel.selectedContactType
        val faculties: LiveData<List<IdDropDownItem>> = this@ContactAdditionViewModel.faculties
        val selectedFaculty: LiveData<IdDropDownItem> =
            this@ContactAdditionViewModel.selectedFaculty
        val groups: LiveData<List<IdDropDownItem>> = this@ContactAdditionViewModel.groups
        val selectedGroup: LiveData<IdDropDownItem> = this@ContactAdditionViewModel.selectedGroup
        val searchFieldValue: LiveData<String> = this@ContactAdditionViewModel.searchFieldValue
        val contacts: LiveData<List<Selectable<UIReceiverDTO>>> =
            this@ContactAdditionViewModel.contacts
    }

    init {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                faculties.postValue(listModel.getFaculties().map { IdDropDownItem(it.id, it.text) })
            } catch (e: Exception) {
                exceptions.value = e
            }
        }
    }

    fun onDismiss() {
        selectedContactType.value = ContactTypeDropDownItem.STUDENT
        clearFields()
    }

    fun onConfirm() {
        contacts.value?.let { selectables ->
            callback(selectables.filter { it.isSelected }.map { it.dto })
        }
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
        viewModelScope.launch(Dispatchers.IO) {
            groups.postValue(
                listModel.getGroupByFaculty(faculty.id).map { IdDropDownItem(it.id, it.text) }
            )
        }
    }

    fun onGroupSelect(group: DropDownItem) {
        selectedGroup.value = group as IdDropDownItem
        viewModelScope.launch(Dispatchers.IO) {
            contacts.postValue(
                listModel.getStudentsByGroup(group.id)
                    .map { Selectable(UIReceiverDTO(it.id, it.text, ReceiverType.STUDENT), false) }
            )
        }
    }

    fun onSearchFieldValueChange(value: String) {
        searchFieldValue.value = value
        viewModelScope.launch(Dispatchers.IO) {
            try {
                contacts.postValue(
                    listModel.getTeacherByQuery(value)
                        .map {
                            Selectable(
                                UIReceiverDTO(it.id, it.text, ReceiverType.TEACHER),
                                false
                            )
                        }
                )
            } catch (e: Exception) {
                exceptions.value = e
            }
        }
    }

    fun onContactSelected(contact: Selectable<UIReceiverDTO>) {
        val current = contacts.value
        contacts.value = null
        contacts.value = current?.let { list ->
            val index = list.indexOf(contact)
            val mutableList = list.toMutableList()
            mutableList.removeAt(index)
            mutableList.add(index, contact.selectedToggle())
            mutableList
        }
    }

    private fun clearFields() {
        contacts.value = listOf()
        searchFieldValue.value = ""
        selectedGroup.value = IdDropDownItem(0, "")
        groups.value = listOf()
        selectedFaculty.value = IdDropDownItem(0, "")
    }
}