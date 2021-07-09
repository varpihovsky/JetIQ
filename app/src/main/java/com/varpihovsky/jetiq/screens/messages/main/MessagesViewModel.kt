package com.varpihovsky.jetiq.screens.messages.main

import androidx.compose.runtime.State
import com.varpihovsky.jetiq.back.model.MessagesModel
import com.varpihovsky.jetiq.system.ConnectionManager
import com.varpihovsky.jetiq.system.JetIQViewModel
import com.varpihovsky.jetiq.system.Refreshable
import com.varpihovsky.jetiq.system.exceptions.Values
import com.varpihovsky.jetiq.system.exceptions.ViewModelExceptionReceivable
import com.varpihovsky.jetiq.system.navigation.NavigationDirections
import com.varpihovsky.jetiq.system.navigation.NavigationManager
import com.varpihovsky.jetiq.system.util.CoroutineDispatchers
import com.varpihovsky.jetiq.system.util.ReactiveTask
import com.varpihovsky.jetiq.ui.appbar.AppbarManager
import com.varpihovsky.jetiq.ui.dto.UIMessageDTO
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import java.util.*
import javax.inject.Inject

@HiltViewModel
class MessagesViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val navigationManager: NavigationManager,
    private val messagesModel: MessagesModel,
    private val connectionManager: ConnectionManager,
    appbarManager: AppbarManager,
) : JetIQViewModel(appbarManager, navigationManager), ViewModelExceptionReceivable, Refreshable {
    val data by lazy { Data() }
    override val isLoading
        get() = messagesModel.isLoading

    private val messages = mutableStateOf(listOf<UIMessageDTO>())
    private val messagesTask =
        ReactiveTask(task = this::collectMessages, dispatcher = dispatchers.IO)

    inner class Data {
        val messages: State<List<UIMessageDTO>> = this@MessagesViewModel.messages
    }

    init {
        messagesTask.start()
        messagesModel.loadMessages()
    }

    private suspend fun collectMessages() {
        messagesModel.getMessagesState().collect { DTOMessages ->
            DTOMessages.map {
                it.toUIDTO()
            }.also {
                messages.value = it
            }
        }
    }

    fun onNewMessageButtonClick() {
        navigationManager.manage(NavigationDirections.newMessage)
    }

    fun onCompose() {
        messagesModel.receivable = this
    }

    fun onDispose() {
        messagesModel.receivable = null
    }

    override fun onRefresh() {
        if (connectionManager.isConnected()) {
            messagesModel.onRefresh()
        } else {
            redirectExceptionToUI(RuntimeException(Values.INTERNET_UNAVAILABLE))
        }
    }

    fun onContactsClick() {
        navigationManager.manage(NavigationDirections.contacts)
    }

}