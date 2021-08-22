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
package com.varpihovsky.feature_new_message

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.varpihovsky.core.dataTransfer.ViewModelDataTransferManager
import com.varpihovsky.core.exceptions.Values
import com.varpihovsky.core.util.CoroutineDispatchers
import com.varpihovsky.core.util.remove
import com.varpihovsky.core_lifecycle.JetIQComponentContext
import com.varpihovsky.core_repo.repo.MessagesRepo
import com.varpihovsky.feature_contacts.ContactsComponent
import com.varpihovsky.feature_contacts.ContactsViewModelData
import com.varpihovsky.repo_data.MessageToSendDTO
import com.varpihovsky.ui_data.dto.UIReceiverDTO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class NewMessageComponent(
    jetIQComponentContext: JetIQComponentContext,
    private val onContactsNavigate: () -> Unit
) : JetIQComponentContext by jetIQComponentContext, KoinComponent {
    val receivers: Value<List<UIReceiverDTO>> by lazy { _receivers }
    val messageFieldValue: Value<String> by lazy { _messageFieldValue }

    private val dispatchers: CoroutineDispatchers by inject()
    private val dataTransferManager: ViewModelDataTransferManager by inject()
    private val messagesRepo: MessagesRepo by inject()

    private val scope = CoroutineScope(Dispatchers.Main)
    private val dataTransferFlow =
        dataTransferManager.getFlowByTag(ContactsComponent.DATA_TRANSFER_TAG)

    private val _receivers = MutableValue(listOf<UIReceiverDTO>())
    private val _messageFieldValue = MutableValue("")

    init {
        scope.launch(dispatchers.IO) { collectTransferredData() }
    }

    private suspend fun collectTransferredData() {
        dataTransferFlow.collect { uncheckedData ->
            if (uncheckedData == null) {
                return@collect
            }

            (uncheckedData as? ContactsViewModelData)?.data?.let {
                _receivers.value = it.sortedBy { it.text }
            }
        }
    }

    fun onReceiverRemove(receiver: UIReceiverDTO) {
        _receivers.value = _receivers.value.remove(receiver)
    }

    fun onNewReceiverButtonClick() {
        dataTransferFlow.value = ContactsViewModelData(_receivers.value, this::class)
        onContactsNavigate()
    }

    fun onMessageValueChange(change: String) {
        _messageFieldValue.value = change
    }

    fun onSendClick() {
        if (!areSendConditionsCompleted()) {
            return
        }

        sendMessage()
    }

    private fun areSendConditionsCompleted(): Boolean = when {
        _receivers.value.isEmpty() -> {
            exceptionController.show(RuntimeException(Values.EMPTY_RECEIVERS))
            false
        }
        _messageFieldValue.value.isEmpty() -> {
            exceptionController.show(RuntimeException(Values.EMPTY_MESSAGE))
            false
        }
        else -> true
    }


    private fun sendMessage() {
        scope.launch(dispatchers.IO) { processSending(_messageFieldValue.value) }
        resetFields()
    }

    private suspend fun processSending(messageBody: String) {
        _receivers.value.map { MessageToSendDTO(it.id, it.type.toInt(), messageBody) }.forEach {
            messagesRepo.sendMessage(it)
        }
    }

    private fun resetFields() {
        _receivers.value = listOf()
        _messageFieldValue.value = ""
    }
}