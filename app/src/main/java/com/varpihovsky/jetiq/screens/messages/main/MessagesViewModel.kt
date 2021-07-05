package com.varpihovsky.jetiq.screens.messages.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.varpihovsky.jetiq.back.model.MessagesModel
import com.varpihovsky.jetiq.system.ConnectionManager
import com.varpihovsky.jetiq.system.JetIQViewModel
import com.varpihovsky.jetiq.system.exceptions.ViewModelExceptionReceivable
import com.varpihovsky.jetiq.system.navigation.NavigationDirections
import com.varpihovsky.jetiq.system.navigation.NavigationManager
import com.varpihovsky.jetiq.system.util.ReactiveTask
import com.varpihovsky.jetiq.ui.appbar.AppbarManager
import com.varpihovsky.jetiq.ui.dto.UIMessageDTO
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import java.util.*
import javax.inject.Inject

@HiltViewModel
class MessagesViewModel @Inject constructor(
    private val navigationManager: NavigationManager,
    private val messagesModel: MessagesModel,
    private val connectionManager: ConnectionManager,
    appbarManager: AppbarManager,
) : JetIQViewModel(appbarManager, navigationManager), ViewModelExceptionReceivable {
    val data by lazy { Data() }
    val isLoading: LiveData<Boolean>
        get() = messagesModel.isLoading

    override val exceptions: MutableStateFlow<Exception?> = MutableStateFlow(null)

    private val messages = MutableLiveData<List<UIMessageDTO>>()
    private val messagesTask = ReactiveTask(task = this::collectMessages)

    inner class Data {
        val messages: LiveData<List<UIMessageDTO>> = this@MessagesViewModel.messages
    }

    init {
        messagesTask.start()
        messagesModel.loadMessages()
    }

    private suspend fun collectMessages() {
        messagesModel.getMessagesState().collect { DTOMessages ->
            DTOMessages.map {
                val split = it.body!!.split("<b>", "</b>:<br>")
                UIMessageDTO(
                    it.msg_id.toInt(),
                    split[1],
                    split[2],
                    Date(it.time.toLong()).toString()
                )
            }.also {
                messages.postValue(it)
            }
        }
    }

    fun onNewMessageButtonClick() {
        //navigationManager.manage(NavigationDirections.newMessage)
    }

    fun onCompose() {
        messagesModel.receivable = this
    }

    fun onDispose() {
        messagesModel.receivable = null
    }

    fun onRefresh() {
        if (connectionManager.isConnected()) {
            messagesModel.loadMessages()
        } else {
            exceptions.value = RuntimeException("Відсутнє підключення до інтернету!")
        }
    }

    fun onContactsClick() {
        navigationManager.manage(NavigationDirections.contacts)
    }

}