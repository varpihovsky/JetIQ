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
package com.varpihovsky.feature_contacts

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.lifecycle.Lifecycle
import com.varpihovsky.core.dataTransfer.ViewModelDataTransferManager
import com.varpihovsky.core.util.CoroutineDispatchers
import com.varpihovsky.core.util.Selectable
import com.varpihovsky.core.util.SelectionEngine
import com.varpihovsky.core.util.selectedOnly
import com.varpihovsky.core_lifecycle.JetIQComponentContext
import com.varpihovsky.core_lifecycle.childContext
import com.varpihovsky.core_repo.repo.ListRepo
import com.varpihovsky.feature_contacts.addition.ContactAdditionComponent
import com.varpihovsky.repo_data.ContactDTO
import com.varpihovsky.ui_data.dto.UIReceiverDTO
import com.varpihovsky.ui_data.mappers.toUIDTO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class ContactsComponent(
    jetIQComponentContext: JetIQComponentContext
) : JetIQComponentContext by jetIQComponentContext, KoinComponent, Lifecycle.Callbacks {
    val contactsAdditionComponent: ContactAdditionComponent get() = checkNotNull(_contactAdditionComponent)

    val searchFieldValue: StateFlow<String> by lazy { _searchFieldValue }
    val isLongClickEnabled: Value<Boolean> by lazy { _isLongClickEnabled }
    val isClickEnabled: Value<Boolean> by lazy { _isClickEnabled }
    val isChoosing: Value<Boolean> by lazy { _isChoosing }
    val isExternalChoosing: Value<Boolean> by lazy { _isExternalChoosing }
    val isAdding: Value<Boolean> by lazy { _isAdding }
    val contacts: Flow<List<Selectable<UIReceiverDTO>>>
        get() = selectionEngine.state
            .combine(searchFieldValue) { contacts, field ->
                contacts
                    .filter { it.dto.text.lowercase().contains(field) }
                    .sortedBy { it.dto.text }
            }

    private val listRepo: ListRepo by inject()
    private val dispatchers: CoroutineDispatchers by inject()
    private val dataTransferManager: ViewModelDataTransferManager by inject()

    private val scope = CoroutineScope(Dispatchers.Main)
    private val dataTransferFlow = dataTransferManager.getFlowByTag(DATA_TRANSFER_TAG)

    private val selectionEngine = SelectionEngine(
        listRepo.getContacts().map { it.map(ContactDTO::toUIDTO) },
        scope,
        dispatchers.IO
    )

    private val _searchFieldValue = MutableStateFlow("")
    private val _isLongClickEnabled = MutableValue(true)
    private val _isClickEnabled = MutableValue(false)
    private val _isChoosing = MutableValue(false)
    private val _isExternalChoosing = MutableValue(false)
    private val _isAdding = MutableValue(false)

    private var _contactAdditionComponent: ContactAdditionComponent? = null

    init {
        scope.launch { collectTransferredData() }
        lifecycle.subscribe(this)
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
        _isExternalChoosing.value = true
        _isChoosing.value = true
        _isClickEnabled.value = true
        _isLongClickEnabled.value = false
    }

    override fun onDestroy() {
        if (_isExternalChoosing.value) {
            transferBack()
        }
        resetState()
    }

    private fun transferBack() {
        dataTransferFlow.value = ContactsViewModelData(
            selectionEngine.state.value.filter { it.isSelected }.map { it.dto },
            this::class
        )
    }

    private fun resetState() {
        selectionEngine.deselectAll()
        _isExternalChoosing.value = false
        setChoosingFalse()
    }

    fun onSearchFieldValueChange(value: String) {
        _searchFieldValue.value = value
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

        onSearchFieldValueChange(_searchFieldValue.value)
    }

    fun onAddClick() {
        _isAdding.value = true
        _contactAdditionComponent = ContactAdditionComponent(childContext("ContactAdditionComponent"))
    }

    fun onRemoveClick() {
        scope.launch(dispatchers.IO) {
            selectionEngine.state.value
                .selectedOnly()
                .map { ContactDTO(it.id, it.text, it.type.name.lowercase()) }
                .forEach { listRepo.removeContact(it) }
            setChoosingFalseIfNotExternal()
        }
    }

    private fun setChoosingTrue() {
        _isClickEnabled.value = true
        _isLongClickEnabled.value = false
        _isChoosing.value = true
    }

    private fun setChoosingFalseIfNotExternal() {
        if (!_isExternalChoosing.value) {
            setChoosingFalse()
        }
    }

    private fun setChoosingFalse() {
        _isLongClickEnabled.value = true
        _isClickEnabled.value = false
        _isChoosing.value = false
    }

    fun onDismissRequest() {
        _isAdding.value = false
        _contactAdditionComponent = null
    }

    fun onConfirmButtonClick(contacts: List<UIReceiverDTO>) {
        scope.launch(dispatchers.IO) {
            contacts
                .map { ContactDTO(it.id, it.text, it.type.name.lowercase()) }
                .forEach { listRepo.addContact(it) }
        }
        onDismissRequest()
    }

    companion object {
        const val DATA_TRANSFER_TAG = "contacts_data_transfer"
    }
}