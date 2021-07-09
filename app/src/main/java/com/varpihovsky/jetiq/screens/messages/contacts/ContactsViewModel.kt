package com.varpihovsky.jetiq.screens.messages.contacts

import androidx.compose.runtime.State
import androidx.lifecycle.viewModelScope
import com.varpihovsky.jetiq.back.dto.ContactDTO
import com.varpihovsky.jetiq.back.model.ListModel
import com.varpihovsky.jetiq.system.JetIQViewModel
import com.varpihovsky.jetiq.system.dataTransfer.ViewModelDataTransferManager
import com.varpihovsky.jetiq.system.navigation.NavigationManager
import com.varpihovsky.jetiq.system.util.CoroutineDispatchers
import com.varpihovsky.jetiq.system.util.ReactiveTask
import com.varpihovsky.jetiq.ui.appbar.AppbarManager
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
    private val dispatchers: CoroutineDispatchers,
    appbarManager: AppbarManager,
    private val navigationManager: NavigationManager,
    private val listModel: ListModel,
    private val dataTransferManager: ViewModelDataTransferManager
) : JetIQViewModel(appbarManager, navigationManager) {
    val data by lazy { Data() }

    private val dataTransferFlow = dataTransferManager.getFlowByTag(DATA_TRANSFER_TAG)

    private var fullContacts: MutableList<Selectable<UIReceiverDTO>> = mutableListOf()
    private val contactsTask =
        ReactiveTask(task = this::collectContacts, dispatcher = dispatchers.IO)
    private val dataTransferTask =
        ReactiveTask(task = this::collectTransferredData, dispatcher = dispatchers.IO)

    private val searchFieldValue = mutableStateOf("")
    private val contacts = mutableStateOf(listOf<Selectable<UIReceiverDTO>>())
    private val isLongClickEnabled = mutableStateOf(true)
    private val isClickEnabled = mutableStateOf(false)
    private val isChoosing = mutableStateOf(false)
    private val isExternalChoosing = mutableStateOf(false)
    private val isAdding = mutableStateOf(false)

    inner class Data {
        val searchFieldValue: State<String> = this@ContactsViewModel.searchFieldValue
        val contacts: State<List<Selectable<UIReceiverDTO>>> = this@ContactsViewModel.contacts
        val isLongClickEnabled: State<Boolean> = this@ContactsViewModel.isLongClickEnabled
        val isClickEnabled: State<Boolean> = this@ContactsViewModel.isClickEnabled
        val isChoosing: State<Boolean> = this@ContactsViewModel.isChoosing
        val isExternalChoosing: State<Boolean> = this@ContactsViewModel.isExternalChoosing
        val isAdding: State<Boolean> = this@ContactsViewModel.isAdding
    }

    init {
        contactsTask.start()
        dataTransferTask.start()
    }

    private suspend fun collectContacts() {
        listModel.getContacts().collect { list ->
            val current = list.map { contactDTO ->
                val receiver = contactDTO.toUIDTO()
                fullContacts.find { it.dto == receiver } ?: Selectable(receiver, false)
            }

            fullContacts = current.toMutableList()

            fullContacts.sortBy { it.dto.text }

            onSearchFieldValueChange(searchFieldValue.value)
        }
    }

    private suspend fun collectTransferredData() {
        dataTransferFlow.collect { uncheckedData ->
            if (uncheckedData == null || uncheckedData.sender == this::class) {
                return@collect
            }

            markExternal()

            val viewModelData = uncheckedData as ContactsViewModelData<*>
            viewModelData.data.forEach { receiver ->
                fullContacts.find { it.dto == receiver }?.let { selectContact(it) }
            }
        }
    }


    private fun markExternal() {
        viewModelScope.launch {
            isExternalChoosing.value = true
            isChoosing.value = true
            isClickEnabled.value = true
            isLongClickEnabled.value = false
        }
    }

    override fun onBackNavButtonClick() {
        if (isExternalChoosing.value) {
            dataTransferFlow.value = ContactsViewModelData(
                fullContacts.filter { it.isSelected }.map { it.dto },
                this::class
            )
        }
        resetState()
        super.onBackNavButtonClick()
    }

    private fun resetState() {
        fullContacts.filter { it.isSelected }.forEach { selectContact(it) }
        isExternalChoosing.value = false
        setChoosingFalse()
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
        selectContact(contact)
    }

    fun onContactClick(contact: Selectable<UIReceiverDTO>) {
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

        searchFieldValue.value.let { onSearchFieldValueChange(it) }
    }

    fun onAddClick() {
        isAdding.value = true
    }

    fun onRemoveClick() {
        viewModelScope.launch(dispatchers.IO) {
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
        isClickEnabled.value = true
        isLongClickEnabled.value = false
        isChoosing.value = false
    }

    private fun setChoosingFalse() {
        isExternalChoosing.value = false
        isLongClickEnabled.value = true
        isClickEnabled.value = false
        isChoosing.value = false
    }

    fun onDismissRequest() {
        isAdding.value = false
    }

    fun onConfirmButtonClick(contacts: List<UIReceiverDTO>) {
        viewModelScope.launch(dispatchers.IO) {
            contacts
                .map { ContactDTO(it.id, it.text, it.type.name.lowercase(Locale.getDefault())) }
                .forEach { listModel.addContact(it) }
        }
        isAdding.value = false
    }

    companion object {
        const val DATA_TRANSFER_TAG = "contacts_data_transfer"
    }
}