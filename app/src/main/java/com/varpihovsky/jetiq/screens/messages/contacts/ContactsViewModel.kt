package com.varpihovsky.jetiq.screens.messages.contacts

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
import com.varpihovsky.core.dataTransfer.ViewModelDataTransferManager
import com.varpihovsky.core.exceptions.ExceptionEventManager
import com.varpihovsky.core.util.*
import com.varpihovsky.core_nav.main.NavigationController
import com.varpihovsky.core_repo.repo.ListRepo
import com.varpihovsky.jetiq.screens.JetIQViewModel
import com.varpihovsky.repo_data.ContactDTO
import com.varpihovsky.ui_data.UIReceiverDTO
import com.varpihovsky.ui_data.mappers.toUIDTO
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class ContactsViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    appbarManager: AppbarManager,
    private val navigationController: NavigationController,
    private val listModel: ListRepo,
    private val dataTransferManager: ViewModelDataTransferManager,
    exceptionEventManager: ExceptionEventManager
) : JetIQViewModel(appbarManager, navigationController, exceptionEventManager) {
    val data by lazy { Data() }

    private val dataTransferFlow = dataTransferManager.getFlowByTag(DATA_TRANSFER_TAG)

    private val dataTransferTask =
        ReactiveTask(task = this::collectTransferredData, dispatcher = dispatchers.IO)

    private val selectionEngine =
        SelectionEngine(
            listModel.getContacts().map { it.map(ContactDTO::toUIDTO) },
            viewModelScope,
            dispatchers.IO
        )

    private val searchFieldValue = MutableStateFlow("")
    private val isLongClickEnabled = mutableStateOf(true)
    private val isClickEnabled = mutableStateOf(false)
    private val isChoosing = mutableStateOf(false)
    private val isExternalChoosing = mutableStateOf(false)
    private val isAdding = mutableStateOf(false)

    inner class Data {
        val searchFieldValue: StateFlow<String> = this@ContactsViewModel.searchFieldValue
        val contacts: Flow<List<Selectable<UIReceiverDTO>>> =
            this@ContactsViewModel.selectionEngine.state.combine(searchFieldValue) { contacts, field ->
                contacts.filter { it.dto.text.lowercase().contains(field) }
                    .sortedBy { it.dto.text }
            }
        val isLongClickEnabled: State<Boolean> = this@ContactsViewModel.isLongClickEnabled
        val isClickEnabled: State<Boolean> = this@ContactsViewModel.isClickEnabled
        val isChoosing: State<Boolean> = this@ContactsViewModel.isChoosing
        val isExternalChoosing: State<Boolean> = this@ContactsViewModel.isExternalChoosing
        val isAdding: State<Boolean> = this@ContactsViewModel.isAdding
    }

    init {
        dataTransferTask.start()
    }

    private suspend fun collectTransferredData() {
        dataTransferFlow.collect { uncheckedData ->
            val viewModelData = uncheckedData as? ContactsViewModelData

            if (viewModelData?.sender == this::class) {
                return@collect
            }

            viewModelData?.data?.let { toSelect ->
                markExternal()
                toSelect.forEach { selectionEngine.toggle(it) }
            }
        }
    }


    private fun markExternal() {
        isExternalChoosing.value = true
        isChoosing.value = true
        isClickEnabled.value = true
        isLongClickEnabled.value = false
    }

    override fun onBackNavButtonClick() {
        if (isExternalChoosing.value) {
            transferBack()
        }
        resetState()
        super.onBackNavButtonClick()
    }

    private fun transferBack() {
        dataTransferFlow.value = ContactsViewModelData(
            selectionEngine.state.value.filter { it.isSelected }.map { it.dto },
            this::class
        )
    }

    private fun resetState() {
        selectionEngine.deselectAll()
        isExternalChoosing.value = false
        setChoosingFalse()
    }

    fun onSearchFieldValueChange(value: String) {
        searchFieldValue.value = value
    }

    fun onContactLongClick(contact: Selectable<UIReceiverDTO>) {
        selectContact(contact)
    }

    fun onContactClick(contact: Selectable<UIReceiverDTO>) {
        selectContact(contact)
    }

    private fun selectContact(contact: Selectable<UIReceiverDTO>) {
        selectionEngine.toggle(contact)

        if (selectionEngine.isAnySelected()) {
            setChoosingTrue()
        } else {
            setChoosingFalseIfNotExternal()
        }

        onSearchFieldValueChange(searchFieldValue.value)
    }

    fun onAddClick() {
        isAdding.value = true
    }

    fun onRemoveClick() {
        viewModelScope.launch(dispatchers.IO) {
            selectionEngine.state.value
                .selectedOnly()
                .map { ContactDTO(it.id, it.text, it.type.name.lowercase()) }
                .forEach { listModel.removeContact(it) }
            setChoosingFalseIfNotExternal()
        }
    }

    private fun setChoosingTrue() {
        isClickEnabled.value = true
        isLongClickEnabled.value = false
        isChoosing.value = true
    }

    private fun setChoosingFalseIfNotExternal() {
        if (!isExternalChoosing.value) {
            setChoosingFalse()
        }
    }

    private fun setChoosingFalse() {
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