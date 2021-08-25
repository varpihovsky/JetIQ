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
package com.varpihovsky.feature_messages.contacts

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.lifecycle.Lifecycle
import com.varpihovsky.core.util.CoroutineDispatchers
import com.varpihovsky.core.util.Selectable
import com.varpihovsky.core.util.SelectionEngine
import com.varpihovsky.core.util.selectedOnly
import com.varpihovsky.core_lifecycle.JetIQComponentContext
import com.varpihovsky.core_lifecycle.childContext
import com.varpihovsky.core_repo.repo.ListRepo
import com.varpihovsky.feature_messages.contacts.addition.ContactAdditionComponent
import com.varpihovsky.repo_data.ContactDTO
import com.varpihovsky.ui_data.dto.ReceiverType
import com.varpihovsky.ui_data.dto.UIReceiverDTO
import com.varpihovsky.ui_data.mappers.toUIDTO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class ContactsComponent(
    jetIQComponentContext: JetIQComponentContext,
    isExternalChoosing: Boolean,
    isUnknownContactOn: Boolean,
    // When isn't choosing
    private val onContactClick: (UIReceiverDTO) -> Unit
) : JetIQComponentContext by jetIQComponentContext, KoinComponent, Lifecycle.Callbacks {
    val contactsAdditionComponent: ContactAdditionComponent get() = checkNotNull(_contactAdditionComponent)

    val searchFieldValue: StateFlow<String> by lazy { _searchFieldValue }
    val isChoosing: Value<Boolean> by lazy { _isChoosing }
    val isExternalChoosing: Value<Boolean> by lazy { _isExternalChoosing }
    val isAdding: Value<Boolean> by lazy { _isAdding }
    val contacts: Flow<List<Selectable<UIReceiverDTO>>>
        get() = selectionEngine.state
            .combine(searchFieldValue) { contacts, field ->
                contacts.filter { it.dto.text.lowercase().contains(field) }
            }

    private val listRepo: ListRepo by inject()
    private val dispatchers: CoroutineDispatchers by inject()

    private val scope = CoroutineScope(Dispatchers.Main)

    private val selectionEngine = SelectionEngine(
        listRepo.getContacts().map {
            if (isUnknownContactOn) {
                listOf(UIReceiverDTO(-1, "Невідомий Контакт", ReceiverType.STUDENT)) +
                        it.map(ContactDTO::toUIDTO).sortedBy { it.text }
            } else {
                it.map(ContactDTO::toUIDTO).sortedBy { it.text }
            }
        },
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
        lifecycle.subscribe(this)
        if (isExternalChoosing) {
            markExternal()
        }
    }


    private fun markExternal() {
        _isExternalChoosing.value = true
        _isChoosing.value = true
        _isClickEnabled.value = true
        _isLongClickEnabled.value = false
    }

    fun onSearchFieldValueChange(value: String) {
        _searchFieldValue.value = value
    }

    fun onContactLongClick(contact: Selectable<UIReceiverDTO>) {
        selectContact(contact)
    }

    fun onContactClick(contact: Selectable<UIReceiverDTO>) {
        if (isChoosing.value) {
            selectContact(contact)
        } else {
            onContactClick(contact.dto)
        }
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
}