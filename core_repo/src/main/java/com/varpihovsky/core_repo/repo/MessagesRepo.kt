package com.varpihovsky.core_repo.repo

import com.varpihovsky.core.Refreshable
import com.varpihovsky.core.exceptions.ModelExceptionSender
import com.varpihovsky.core.exceptions.ViewModelExceptionReceivable
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

    suspend fun sendMessage(messageToSendDTO: MessageToSendDTO)

    fun clear()

    companion object {
        operator fun invoke(
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
        wrapException(
            result = jetIQMessageManager.getMessages(requireSession()),
            onSuccess = { addMessagesToDatabase(it.value) },
        )
        isLoading.value = false
    }

    private fun addMessagesToDatabase(messages: List<MessageDTO>) {
        messages.forEach {
            if (it.body != null) {
                messageDAO.addMessage(it)
            }
        }
    }

    override fun clear() {
        messageDAO.deleteAll()
    }

    override suspend fun sendMessage(messageToSendDTO: MessageToSendDTO) {
        requireSession().let { session ->
            val csrf = wrapException(
                result = jetIQMessageManager.getCsrf(session),
                onSuccess = { it.value },
                onFailure = { return@let }
            )
            wrapException(result = jetIQMessageManager.sendMessage(session, csrf, messageToSendDTO))
        }
    }
}