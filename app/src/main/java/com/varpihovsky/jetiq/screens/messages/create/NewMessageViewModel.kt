package com.varpihovsky.jetiq.screens.messages.create

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.varpihovsky.jetiq.back.dto.MessageToSendDTO
import com.varpihovsky.jetiq.back.model.MessagesModel
import com.varpihovsky.jetiq.screens.messages.contacts.ContactsViewModel
import com.varpihovsky.jetiq.screens.messages.contacts.ContactsViewModelData
import com.varpihovsky.jetiq.system.JetIQViewModel
import com.varpihovsky.jetiq.system.dataTransfer.ViewModelDataTransferManager
import com.varpihovsky.jetiq.system.exceptions.ViewModelWithException
import com.varpihovsky.jetiq.system.navigation.NavigationDirections
import com.varpihovsky.jetiq.system.navigation.NavigationManager
import com.varpihovsky.jetiq.system.util.ReactiveTask
import com.varpihovsky.jetiq.ui.appbar.AppbarManager
import com.varpihovsky.jetiq.ui.dto.UIReceiverDTO
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewMessageViewModel @Inject constructor(
    appbarManager: AppbarManager,
    private val navigationManager: NavigationManager,
    viewModelDataTransferManager: ViewModelDataTransferManager,
    private val messagesModel: MessagesModel
) : JetIQViewModel(appbarManager, navigationManager), ViewModelWithException {
    val data by lazy { Data() }

    override val exceptions: MutableStateFlow<Exception?> = MutableStateFlow(null)

    private val dataTransferFlow =
        viewModelDataTransferManager.getFlowByTag(ContactsViewModel.DATA_TRANSFER_TAG)
    private val dataTransferTask = ReactiveTask(task = this::collectTransferredData)


    private val receivers = MutableLiveData<List<UIReceiverDTO>>()
    private val messageFieldValue = MutableLiveData("")

    inner class Data {
        val receivers: LiveData<List<UIReceiverDTO>> = this@NewMessageViewModel.receivers
        val messageFieldValue: LiveData<String> = this@NewMessageViewModel.messageFieldValue
    }

    init {
        dataTransferTask.start()
    }

    private suspend fun collectTransferredData() {
        dataTransferFlow.collect { uncheckedData ->
            Log.d(
                "Appl",
                (uncheckedData == null).toString() + " " + (uncheckedData?.sender == this::class).toString() + " " + uncheckedData.toString() + "${uncheckedData?.data} ${uncheckedData?.sender}"
            )
            if (uncheckedData == null || uncheckedData.sender == this::class) {
                return@collect
            }

            viewModelScope.launch {
                receivers.value = null
                receivers.value = (uncheckedData as ContactsViewModelData<*>).data
            }
        }
    }

    fun onReceiverRemove(receiver: UIReceiverDTO) {
        val result = receivers.value?.let {
            val mutable = it.toMutableList()
            mutable.remove(receiver)
            mutable
        }
        receivers.value = null
        receivers.value = result
    }

    fun onNewReceiverButtonClick() {
        dataTransferFlow.value = ContactsViewModelData(receivers.value ?: listOf(), this::class)
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

    private fun areSendConditionsCompleted(): Boolean {
        if (receivers.value?.isEmpty() == true) {
            exceptions.value = RuntimeException("Необхідно додати хочаб одного отримувача")
            return false
        }
        if (messageFieldValue.value?.isEmpty() == true) {
            exceptions.value = RuntimeException("Повідомлення не може бути пустим")
            return false
        }
        return true
    }

    private fun sendMessage() {
        val messageBody = messageFieldValue.value!!
        receivers.value?.map { MessageToSendDTO(it.id, it.type, messageBody) }?.forEach {
            messagesModel.sendMessage(it)
        }
    }
}