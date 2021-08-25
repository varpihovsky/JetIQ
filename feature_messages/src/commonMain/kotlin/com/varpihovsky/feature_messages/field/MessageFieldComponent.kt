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
package com.varpihovsky.feature_messages.field

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.varpihovsky.core.exceptions.Values
import com.varpihovsky.core.util.CoroutineDispatchers
import com.varpihovsky.core_repo.repo.MessagesRepo
import com.varpihovsky.feature_messages.MessagesComponentContext
import com.varpihovsky.repo_data.MessageToSendDTO
import com.varpihovsky.ui_data.dto.UIReceiverDTO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

internal class MessageFieldComponent(
    messagesComponentContext: MessagesComponentContext,
    private val receivers: Value<List<UIReceiverDTO>>
) : MessagesComponentContext by messagesComponentContext, KoinComponent {
    val messageFieldValue: Value<String> by lazy { _messageFieldValue }

    private val dispatchers: CoroutineDispatchers by inject()
    private val messagesRepo: MessagesRepo by inject()

    private val scope = CoroutineScope(Dispatchers.Main)

    private val _messageFieldValue = MutableValue("")

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
        receivers.value.isEmpty() -> {
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
        receivers.value.map { MessageToSendDTO(it.id, it.type.toInt(), messageBody, Clock.System.now().epochSeconds) }
            .forEach {
                messagesRepo.sendMessage(it)
            }
    }

    private fun resetFields() {
        _messageFieldValue.value = ""
    }
}