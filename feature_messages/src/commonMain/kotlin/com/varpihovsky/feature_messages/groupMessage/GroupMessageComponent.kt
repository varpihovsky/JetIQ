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
package com.varpihovsky.feature_messages.groupMessage

import androidx.compose.runtime.mutableStateOf
import com.arkivanov.decompose.value.MutableValue
import com.varpihovsky.feature_messages.MessagesComponentContext
import com.varpihovsky.feature_messages.childContext
import com.varpihovsky.feature_messages.contacts.ContactsComponent
import com.varpihovsky.feature_messages.contacts.chosen.ChosenContactsComponent
import com.varpihovsky.feature_messages.field.MessageFieldComponent
import com.varpihovsky.ui_data.dto.UIReceiverDTO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

internal class GroupMessageComponent(
    messagesComponentContext: MessagesComponentContext
) : MessagesComponentContext by messagesComponentContext {
    val chosenContactsComponent by lazy {
        ChosenContactsComponent(
            childContext("ChosenContactsComponent"),
            contacts,
            ::onAddContactClick,
            ::onRemoveContactClick
        )
    }
    val messageFieldComponent by lazy {
        MessageFieldComponent(
            childContext("MessageFieldComponent"),
            contacts
        )
    }
    val contactsComponent = mutableStateOf<ContactsComponent?>(null)
    private val contacts = MutableValue(listOf<UIReceiverDTO>())
    private val scope = CoroutineScope(Dispatchers.Unconfined)
    private var collectionJob: Job? = null
    private var collectedContacts: List<UIReceiverDTO> = listOf()

    init {
        // Currently, contacts component can be shown as dialog (on mobile) and separate screen (on jvm),
        // so, we still need to process both standalone methods and back press dispatcher.
        backPressedDispatcher.register {
            if (contactsComponent.value != null) {
                onAcceptButtonClick()
                return@register true
            }
            false
        }
    }

    private fun onAddContactClick() {
        contactsComponent.value = ContactsComponent(
            childContext("ContactsComponent"),
            isExternalChoosing = true,
            isUnknownContactOn = false
        ) {}
        collectionJob = scope.launch {
            contactsComponent.value?.contacts?.collect { contacts ->
                collectedContacts = contacts.filter { it.isSelected }.map { it.dto }
            }
        }
    }

    private fun onRemoveContactClick(contact: UIReceiverDTO) {
        contacts.value = contacts.value - contact
    }

    fun onAcceptButtonClick() {
        contacts.value = contacts.value + collectedContacts.filter { !contacts.value.contains(it) }
        onDismissRequest()
    }

    fun onDismissRequest() {
        contactsComponent.value = null
        collectionJob?.cancel()
        collectionJob = null
    }
}