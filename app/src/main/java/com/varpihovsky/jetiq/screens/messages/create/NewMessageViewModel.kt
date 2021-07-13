package com.varpihovsky.jetiq.screens.messages.create

import androidx.compose.runtime.State
import androidx.lifecycle.viewModelScope
import com.varpihovsky.core.dataTransfer.ViewModelDataTransferManager
import com.varpihovsky.core.exceptions.Values
import com.varpihovsky.core.exceptions.ViewModelExceptionReceivable
import com.varpihovsky.core.exceptions.ViewModelWithException
import com.varpihovsky.core.navigation.NavigationDirections
import com.varpihovsky.core.navigation.NavigationManager
import com.varpihovsky.core.util.CoroutineDispatchers
import com.varpihovsky.core.util.ReactiveTask
import com.varpihovsky.core.util.remove
import com.varpihovsky.core_repo.repo.MessagesRepo
import com.varpihovsky.jetiq.appbar.AppbarManager
import com.varpihovsky.jetiq.screens.JetIQViewModel
import com.varpihovsky.jetiq.screens.messages.contacts.ContactsViewModel
import com.varpihovsky.jetiq.screens.messages.contacts.ContactsViewModelData
import com.varpihovsky.repo_data.MessageToSendDTO
import com.varpihovsky.ui_data.UIReceiverDTO
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewMessageViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    appbarManager: AppbarManager,
    private val navigationManager: NavigationManager,
    viewModelDataTransferManager: ViewModelDataTransferManager,
    private val messagesModel: MessagesRepo
) : JetIQViewModel(appbarManager, navigationManager), ViewModelWithException,
    ViewModelExceptionReceivable {
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

    override fun onCompose() {
        messagesModel.receivable = this
    }

    override fun onDispose() {
        messagesModel.receivable = null
    }

    private suspend fun collectTransferredData() {
        dataTransferFlow.collect { uncheckedData ->
            if (uncheckedData == null) {
                return@collect
            }

            (uncheckedData as? ContactsViewModelData)?.data?.let {
                receivers.value = it
            }
        }
    }

    fun onReceiverRemove(receiver: UIReceiverDTO) {
        receivers.value = receivers.value.remove(receiver)
    }

    fun onNewReceiverButtonClick() {
        dataTransferFlow.value = ContactsViewModelData(receivers.value)
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
        receivers.value.map { MessageToSendDTO(it.id, it.type.toInt(), messageBody) }.forEach {
            messagesModel.sendMessage(it)
        }
    }

    private fun resetFields() {
        receivers.value = listOf()
        messageFieldValue.value = ""
    }
}