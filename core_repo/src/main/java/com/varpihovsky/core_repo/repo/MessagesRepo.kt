package com.varpihovsky.core_repo.repo

import com.varpihovsky.core.Refreshable
import com.varpihovsky.core.exceptions.ModelExceptionSender
import com.varpihovsky.core.exceptions.ViewModelExceptionReceivable
import com.varpihovsky.core.util.logException
import com.varpihovsky.core_db.dao.ConfidentialDAO
import com.varpihovsky.core_db.dao.MessageDAO
import com.varpihovsky.core_db.dao.ProfileDAO
import com.varpihovsky.core_network.managers.JetIQMessageManager
import com.varpihovsky.repo_data.MessageDTO
import com.varpihovsky.repo_data.MessageToSendDTO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

interface MessagesRepo : Refreshable, ModelExceptionSender {
    fun loadMessages()
    fun getMessages(): Flow<List<MessageDTO>>
    fun sendMessage(messageToSendDTO: MessageToSendDTO)
    fun clear()

    companion object {
        internal operator fun invoke(
            jetIQMessageManager: JetIQMessageManager,
            messageDAO: MessageDAO,
            confidentialDAO: ConfidentialDAO,
            profileDAO: ProfileDAO
        ): MessagesRepo = MessagesRepoImpl(
            jetIQMessageManager,
            messageDAO,
            confidentialDAO,
            profileDAO
        )
    }
}

private class MessagesRepoImpl @Inject constructor(
    private val jetIQMessageManager: JetIQMessageManager,
    private val messageDAO: MessageDAO,
    confidentialDAO: ConfidentialDAO,
    profileDAO: ProfileDAO,
) : ConfidentRepo(confidentialDAO, profileDAO), MessagesRepo {
    override var receivable: ViewModelExceptionReceivable? = null
    override val isLoading
        get() = _isLoading

    override fun onRefresh() {
        loadMessages()
    }

    private val _isLoading = mutableStateOf(false)

    override fun loadMessages() {
        _isLoading.value = true
        modelScope.launch { processMessagesLoading() }
    }

    override fun getMessages() = messageDAO.getMessages()

    private suspend fun processMessagesLoading() {
        try {
            jetIQMessageManager.getMessages(requireSession()).forEach { messageDTO ->
                if (messageDTO.body != null) {
                    messageDAO.addMessage(messageDTO)
                }
            }
        } catch (e: RuntimeException) {
            receivable?.send(e)
            logException(e, getDebugPrefix(this) ?: "Application")
        }
        isLoading.value = false
    }

    override fun clear() {
        messageDAO.deleteAll()
    }

    override fun sendMessage(messageToSendDTO: MessageToSendDTO) {
        requireSession().let {
            val csrf = jetIQMessageManager.getCsrf(it)
            jetIQMessageManager.sendMessage(it, csrf, messageToSendDTO)
        }
    }
}