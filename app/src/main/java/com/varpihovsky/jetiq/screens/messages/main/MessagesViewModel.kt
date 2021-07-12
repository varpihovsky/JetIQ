package com.varpihovsky.jetiq.screens.messages.main

import androidx.compose.runtime.State
import com.varpihovsky.core.ConnectionManager
import com.varpihovsky.core.Refreshable
import com.varpihovsky.core.exceptions.Values
import com.varpihovsky.core.exceptions.ViewModelExceptionReceivable
import com.varpihovsky.core.navigation.NavigationDirections
import com.varpihovsky.core.navigation.NavigationManager
import com.varpihovsky.core.util.CoroutineDispatchers
import com.varpihovsky.core.util.ReactiveTask
import com.varpihovsky.core_repo.repo.MessagesRepo
import com.varpihovsky.jetiq.appbar.AppbarManager
import com.varpihovsky.jetiq.screens.JetIQViewModel
import com.varpihovsky.ui_data.UIMessageDTO
import com.varpihovsky.ui_data.mappers.toUIDTO
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import java.util.*
import javax.inject.Inject

@HiltViewModel
class MessagesViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val navigationManager: NavigationManager,
    private val messagesModel: MessagesRepo,
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
        messagesModel.getMessages().collect { DTOMessages ->
            DTOMessages.map { it.toUIDTO() }.also { messages.value = it }
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