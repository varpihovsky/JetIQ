package com.varpihovsky.jetiq.screens.messages.create

import androidx.compose.runtime.State
import androidx.lifecycle.viewModelScope
import com.varpihovsky.jetiq.back.dto.MessageToSendDTO
import com.varpihovsky.jetiq.back.model.MessagesModel
import com.varpihovsky.jetiq.screens.messages.contacts.ContactsViewModel
import com.varpihovsky.jetiq.screens.messages.contacts.ContactsViewModelData
import com.varpihovsky.jetiq.system.JetIQViewModel
import com.varpihovsky.jetiq.system.dataTransfer.ViewModelDataTransferManager
import com.varpihovsky.jetiq.system.exceptions.Values
import com.varpihovsky.jetiq.system.exceptions.ViewModelWithException
import com.varpihovsky.jetiq.system.navigation.NavigationDirections
import com.varpihovsky.jetiq.system.navigation.NavigationManager
import com.varpihovsky.jetiq.system.util.CoroutineDispatchers
import com.varpihovsky.jetiq.system.util.ReactiveTask
import com.varpihovsky.jetiq.system.util.remove
import com.varpihovsky.jetiq.ui.appbar.AppbarManager
import com.varpihovsky.jetiq.ui.dto.UIReceiverDTO
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewMessageViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    appbarManager: AppbarManager,
    private val navigationManager: NavigationManager,
    viewModelDataTransferManager: ViewModelDataTransferManager,
    private val messagesModel: MessagesModel
) : JetIQViewModel(appbarManager, navigationManager), ViewModelWithException {
    val data by lazy { Data() }

    override val exceptions: MutableStateFlow<Exception?> = MutableStateFlow(null)

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

    private suspend fun collectTransferredData() {
        dataTransferFlow.collect { uncheckedData ->
            if (uncheckedData == null || uncheckedData.sender == this::class) {
                return@collect
            }

            receivers.value = (uncheckedData as ContactsViewModelData<*>).data
        }
    }

    fun onReceiverRemove(receiver: UIReceiverDTO) {
        receivers.value = receivers.value.remove(receiver)
    }

    fun onNewReceiverButtonClick() {
        dataTransferFlow.value = ContactsViewModelData(receivers.value, this::class)
        navigationManager.manage(NavigationDirections.contacts)
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
        receivers.value.map { MessageToSendDTO(it.id, it.type, messageBody) }.forEach {
            messagesModel.sendMessage(it)
        }
    }

    private fun resetFields() {
        receivers.value = listOf()
        messageFieldValue.value = ""
    }
}