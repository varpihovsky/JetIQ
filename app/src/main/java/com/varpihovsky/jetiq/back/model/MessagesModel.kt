package com.varpihovsky.jetiq.back.model

import com.varpihovsky.jetiq.back.api.managers.JetIQMessageManager
import com.varpihovsky.jetiq.back.db.managers.ConfidentialDatabaseManager
import com.varpihovsky.jetiq.back.db.managers.MessageDatabaseManager
import com.varpihovsky.jetiq.back.db.managers.ProfileDatabaseManager
import com.varpihovsky.jetiq.back.dto.MessageToSendDTO
import com.varpihovsky.jetiq.system.Refreshable
import com.varpihovsky.jetiq.system.exceptions.ModelExceptionSender
import com.varpihovsky.jetiq.system.exceptions.ViewModelExceptionReceivable
import com.varpihovsky.jetiq.system.util.ThreadSafeMutableState
import com.varpihovsky.jetiq.system.util.logException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

class MessagesModel @Inject constructor(
    private val jetIQMessageManager: JetIQMessageManager,
    private val messageDatabaseManager: MessageDatabaseManager,
    confidentialDatabaseManager: ConfidentialDatabaseManager,
    profileDatabaseManager: ProfileDatabaseManager
) : ConfidentModel(confidentialDatabaseManager, profileDatabaseManager), ModelExceptionSender,
    Refreshable {
    override var receivable: ViewModelExceptionReceivable? = null
    override val isLoading
        get() = _isLoading

    override fun onRefresh() {
        loadMessages()
    }

    private val _isLoading = ThreadSafeMutableState(false, modelScope)

    fun loadMessages() {
        _isLoading.value = true
        modelScope.launch { processMessagesLoading() }
    }

    fun getMessagesState() = messageDatabaseManager.getAll()

    private suspend fun processMessagesLoading() {
        try {
            jetIQMessageManager.getMessages(requireSession()).forEach { messageDTO ->
                if (messageDTO.body != null) {
                    delay(100)
                    messageDatabaseManager.putMessage(messageDTO)
                }
            }
        } catch (e: RuntimeException) {
            receivable?.send(e)
            logException(e, getDebugPrefix(this) ?: "Application")
        }
        modelScope.launch(Dispatchers.Main) { isLoading.value = false }
    }

    fun clearData() {
        messageDatabaseManager.removeAll()
    }

    fun sendMessage(messageToSendDTO: MessageToSendDTO) {
        requireSession().let {
            val csrf = jetIQMessageManager.getCsrf(it)
            jetIQMessageManager.sendMessage(it, csrf, messageToSendDTO)
        }
    }
}