package com.varpihovsky.feature_new_message

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
import com.varpihovsky.core.appbar.AppbarManager
import com.varpihovsky.core.dataTransfer.ViewModelDataTransferManager
import com.varpihovsky.core.exceptions.ExceptionEventManager
import com.varpihovsky.core.exceptions.Values
import com.varpihovsky.core.lifecycle.viewModelScope
import com.varpihovsky.core.util.CoroutineDispatchers
import com.varpihovsky.core.util.ReactiveTask
import com.varpihovsky.core.util.remove
import com.varpihovsky.core_lifecycle.JetIQViewModel
import com.varpihovsky.core_lifecycle.mutableStateOf
import com.varpihovsky.core_lifecycle.redirectExceptionToUI
import com.varpihovsky.core_nav.main.NavigationController
import com.varpihovsky.core_nav.navigation.NavigationDirections
import com.varpihovsky.core_repo.repo.MessagesRepo
import com.varpihovsky.feature_contacts.ContactsViewModel
import com.varpihovsky.feature_contacts.ContactsViewModelData
import com.varpihovsky.repo_data.MessageToSendDTO
import com.varpihovsky.ui_data.dto.UIReceiverDTO
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class NewMessageViewModel(
    private val dispatchers: CoroutineDispatchers,
    appbarManager: AppbarManager,
    override val navigationController: NavigationController,
    viewModelDataTransferManager: ViewModelDataTransferManager,
    private val messagesModel: MessagesRepo,
    exceptionEventManager: ExceptionEventManager
) : JetIQViewModel(appbarManager, navigationController, exceptionEventManager) {
    val data by lazy { Data() }

    private val dataTransferFlow =
        viewModelDataTransferManager.getFlowByTag(ContactsViewModel.DATA_TRANSFER_TAG)
    private val dataTransferTask =
        ReactiveTask(task = this::collectTransferredData, dispatcher = dispatchers.IO)

    private val receivers = mutableStateOf(listOf<UIReceiverDTO>())
    private val messageFieldValue = mutableStateOf("")

    inner class Data {
        val receivers: State<List<UIReceiverDTO>> = this@NewMessageViewModel.receivers
        val messageFieldValue: State<String> = this@NewMessageViewModel.messageFieldValue
    }

    init {
        dataTransferTask.start()
    }

    override fun onBackNavButtonClick() {
        receivers.value = listOf()
        super.onBackNavButtonClick()
    }

    private suspend fun collectTransferredData() {
        dataTransferFlow.collect { uncheckedData ->
            if (uncheckedData == null) {
                return@collect
            }

            (uncheckedData as? ContactsViewModelData)?.data?.let {
                receivers.value = it.sortedBy { it.text }
            }
        }
    }

    fun onReceiverRemove(receiver: UIReceiverDTO) {
        receivers.value = receivers.value.remove(receiver)
    }

    fun onNewReceiverButtonClick() {
        dataTransferFlow.value = ContactsViewModelData(receivers.value, this::class)
        navigationController.manage(NavigationDirections.contacts.destination)
    }

    fun onMessageValueChange(change: String) {
        messageFieldValue.value = change
    }

    fun onSendClick() {
        if (!areSendConditionsCompleted()) {
            return
        }

        sendMessage()
    }

    private fun areSendConditionsCompleted(): Boolean = when {
        receivers.value.isEmpty() -> {
            redirectExceptionToUI(RuntimeException(Values.EMPTY_RECEIVERS))
            false
        }
        messageFieldValue.value.isEmpty() -> {
            redirectExceptionToUI(RuntimeException(Values.EMPTY_MESSAGE))
            false
        }
        else -> true
    }


    private fun sendMessage() {
        viewModelScope.launch(dispatchers.IO) { processSending(messageFieldValue.value) }
        resetFields()
    }

    private suspend fun processSending(messageBody: String) {
        receivers.value.map { MessageToSendDTO(it.id, it.type.toInt(), messageBody) }.forEach {
            messagesModel.sendMessage(it)
        }
    }

    private fun resetFields() {
        receivers.value = listOf()
        messageFieldValue.value = ""
    }
}