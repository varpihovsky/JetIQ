package com.varpihovsky.jetiq.screens.messages.contacts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.varpihovsky.jetiq.back.dto.ContactDTO
import com.varpihovsky.jetiq.back.model.ListModel
import com.varpihovsky.jetiq.system.JetIQViewModel
import com.varpihovsky.jetiq.system.navigation.NavigationManager
import com.varpihovsky.jetiq.system.util.ReactiveTask
import com.varpihovsky.jetiq.ui.appbar.AppbarManager
import com.varpihovsky.jetiq.ui.dto.ReceiverType
import com.varpihovsky.jetiq.ui.dto.UIReceiverDTO
import com.varpihovsky.jetiq.ui.dto.func_extensions.Selectable
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class ContactsViewModel @Inject constructor(
    appbarManager: AppbarManager,
    private val navigationManager: NavigationManager,
    private val listModel: ListModel,
) : JetIQViewModel(appbarManager, navigationManager) {
    val data by lazy { Data() }

    private var fullContacts: MutableList<Selectable<UIReceiverDTO>> = mutableListOf()
    private val contactsTask = ReactiveTask(task = this::collectContacts)

    private val searchFieldValue = MutableLiveData("")
    private val contacts = MutableLiveData<List<Selectable<UIReceiverDTO>>>()
    private val isLongClickEnabled = MutableLiveData(true)
    private val isClickEnabled = MutableLiveData(false)
    private val isChoosing = MutableLiveData(false)
    private val isExternalChoosing = MutableLiveData(false)
    private val isAdding = MutableLiveData(false)

    inner class Data {
        val searchFieldValue: LiveData<String> = this@ContactsViewModel.searchFieldValue
        val contacts: LiveData<List<Selectable<UIReceiverDTO>>> = this@ContactsViewModel.contacts
        val isLongClickEnabled: LiveData<Boolean> = this@ContactsViewModel.isLongClickEnabled
        val isClickEnabled: LiveData<Boolean> = this@ContactsViewModel.isClickEnabled
        val isChoosing: LiveData<Boolean> = this@ContactsViewModel.isChoosing
        val isExternalChoosing: LiveData<Boolean> = this@ContactsViewModel.isExternalChoosing
        val isAdding: LiveData<Boolean> = this@ContactsViewModel.isAdding
    }

    init {
        contactsTask.start()
    }

    private suspend fun collectContacts() {
        listModel.getContacts().collect { list ->
            val current = list.map { contactDTO ->
                val receiver = UIReceiverDTO(
                    contactDTO.id,
                    contactDTO.text,
                    when (contactDTO.type) {
                        ContactDTO.TYPE_STUDENT -> ReceiverType.STUDENT
                        ContactDTO.TYPE_TEACHER -> ReceiverType.TEACHER
                        else -> throw RuntimeException()
                    }
                )
                fullContacts.find { it.dto == receiver } ?: Selectable(receiver, false)
            }

            fullContacts = current.toMutableList()

            fullContacts.sortBy { it.dto.text }

            searchFieldValue.value?.let { onSearchFieldValueChange(it) }
        }
    }

    override fun onBackNavButtonClick() {
        resetState { super.onBackNavButtonClick() }
    }

    private fun resetState(onInternal: () -> Unit = {}, onExternal: () -> Unit) {
        if (isExternalChoosing.value == false) {
            if (fullContacts.any { it.isSelected }) {
                fullContacts = fullContacts.map { Selectable(it.dto, false) }.toMutableList()
                onInternal()
            }
            onExternal()
        } else {
            isExternalChoosing.value = true
            onExternal()
        }
    }

    fun onSearchFieldValueChange(value: String) {
        viewModelScope.launch {
            if (value != searchFieldValue.value) {
                searchFieldValue.value = value
            }

            contacts.value = listOf()

            if (value.isEmpty()) {
                contacts.value = fullContacts
                return@launch
            }

            contacts.value =
                fullContacts.filter {
                    it.dto.text.lowercase(Locale.getDefault())
                        .contains(value.lowercase(Locale.getDefault()))
                }
        }
    }

    fun onContactLongClick(contact: Selectable<UIReceiverDTO>) {
        if (isLongClickEnabled.value == false) {
            return
        }

        selectContact(contact)
    }

    fun onContactClick(contact: Selectable<UIReceiverDTO>) {
        if (isClickEnabled.value == false) {
            return
        }

        selectContact(contact)
    }

    private fun selectContact(contact: Selectable<UIReceiverDTO>) {
        val index = fullContacts.indexOf(contact)
        fullContacts.removeAt(index)
        fullContacts.add(index, contact.selectedToggle())

        if (fullContacts.any { it.isSelected }) {
            setChoosingTrue()
        } else {
            setChoosingFalse()
        }

        searchFieldValue.value?.let { onSearchFieldValueChange(it) }
    }

    fun onAddClick() {
        isAdding.value = true
    }

    fun onRemoveClick() {
        viewModelScope.launch(Dispatchers.IO) {
            fullContacts
                .filter { it.isSelected }
                .map {
                    ContactDTO(
                        it.dto.id, it.dto.text,
                        it.dto.type.name.lowercase(Locale.getDefault())
                    )
                }
                .forEach { listModel.removeContact(it) }
            launch(Dispatchers.Main) { setChoosingFalse() }
        }

    }

    private fun setChoosingTrue() {
        isClickEnabled.postValue(true)
        isLongClickEnabled.postValue(false)
        isChoosing.postValue(true)
    }

    private fun setChoosingFalse() {
        isLongClickEnabled.postValue(true)
        if (isExternalChoosing.value == false) {
            isClickEnabled.postValue(false)
            isChoosing.postValue(false)
        }
    }

    fun onDismissRequest() {
        isAdding.value = false
    }

    fun onConfirmButtonClick(contacts: List<UIReceiverDTO>) {
        viewModelScope.launch(Dispatchers.IO) {
            contacts
                .map { ContactDTO(it.id, it.text, it.type.name.lowercase(Locale.getDefault())) }
                .forEach { listModel.addContact(it) }
        }
        isAdding.value = false
    }
}