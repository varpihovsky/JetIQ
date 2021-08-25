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
package com.varpihovsky.core_repo.repo

import com.varpihovsky.core.Refreshable
import com.varpihovsky.core.exceptions.ExceptionEventManager
import com.varpihovsky.core.log.e
import com.varpihovsky.core_db.dao.ConfidentialDAO
import com.varpihovsky.core_db.dao.MessageDAO
import com.varpihovsky.core_db.dao.ProfileDAO
import com.varpihovsky.jetiqApi.Api
import com.varpihovsky.jetiqApi.data.Message
import com.varpihovsky.repo_data.MessageToSendDTO
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withTimeout

/**
 * Interface used for loading and sending messages.
 *
 * @author Vladyslav Podrezenko
 */
interface MessagesRepo : Refreshable {
    /**
     * Requests messages from server and adds missing. If message is present in database - ignores.
     */
    suspend fun loadMessages()

    /**
     * Returns flow of current messages in database. To load messages use [loadMessages] method.
     *
     * @return list of [MessageDTO]
     */
    fun getMessages(): Flow<List<Message>>

    /**
     * Sends message to user specified in [MessageToSendDTO].
     *
     * @param messageToSendDTO contains all message-related data.
     */
    suspend fun sendMessage(messageToSendDTO: MessageToSendDTO)

    /**
     * Clears database.
     */
    fun clear()

    fun isMessageRead(id: Int): Flow<Boolean>

    fun isMessageWasRead(id: Int): Boolean

    fun setMessageRead(id: Int)

    fun getSentMessages(receiverId: Int, receiverType: Int): Flow<List<MessageToSendDTO>>

    companion object {
        operator fun invoke(
            api: Api,
            messageDAO: MessageDAO,
            confidentialDAO: ConfidentialDAO,
            profileDAO: ProfileDAO,
            exceptionEventManager: ExceptionEventManager
        ): MessagesRepo = MessagesRepoImpl(
            api,
            messageDAO,
            confidentialDAO,
            profileDAO,
            exceptionEventManager
        )
    }
}

private class MessagesRepoImpl constructor(
    private val api: Api,
    private val messageDAO: MessageDAO,
    confidentialDAO: ConfidentialDAO,
    profileDAO: ProfileDAO,
    exceptionEventManager: ExceptionEventManager
) : ConfidentRepo(confidentialDAO, profileDAO, exceptionEventManager), MessagesRepo {
    override val isLoading
        get() = _isLoading

    override suspend fun onRefresh() {
        loadMessages()
    }

    private val _isLoading = mutableStateOf(false)

    override suspend fun loadMessages() {

        _isLoading.value = true

        try {
            launchWithTimeout()
        } catch (e: Exception) {
            e(e.stackTraceToString())
        }

        delay(LOADING_DELAY)

        _isLoading.value = false
    }

    private suspend fun launchWithTimeout() {
        withTimeout(LOADING_TIMEOUT) {
            processMessagesLoading()
        }
    }

    private suspend fun processMessagesLoading() {
        wrapException(
            result = api.getMessages(requireSession()),
            onSuccess = { addMessagesToDatabase(it.value) },
        )
    }

    private fun addMessagesToDatabase(messages: List<Message>) {
        messages.forEach {
            if (it.body != null) {
                messageDAO.addMessage(it)
            }
        }
    }

    override fun getMessages() = messageDAO.getMessages()

    override fun clear() {
        messageDAO.deleteAll()
    }

    override fun isMessageRead(id: Int): Flow<Boolean> {
        return messageDAO.isMessageRead(id)
    }

    override fun isMessageWasRead(id: Int): Boolean {
        return messageDAO.isMessageWasRead(id)
    }

    override fun setMessageRead(id: Int) {
        messageDAO.setMessageRead(id)
    }

    override fun getSentMessages(receiverId: Int, receiverType: Int): Flow<List<MessageToSendDTO>> {
        return messageDAO.getSentMessages(receiverId, receiverType)
    }

    override suspend fun sendMessage(messageToSendDTO: MessageToSendDTO) {
        requireSession().let { session ->
            val csrf = wrapException(
                result = api.getCsrf(session),
                onSuccess = { it.value },
                onFailure = { return@let }
            )
            wrapException(
                result = api.sendMessage(
                    session,
                    messageToSendDTO.id,
                    messageToSendDTO.type,
                    messageToSendDTO.body,
                    csrf
                )
            )
            messageDAO.addSentMessage(messageToSendDTO)
        }
    }

    companion object {
        private const val LOADING_DELAY = 500L
        private const val LOADING_TIMEOUT = 15000L
    }
}