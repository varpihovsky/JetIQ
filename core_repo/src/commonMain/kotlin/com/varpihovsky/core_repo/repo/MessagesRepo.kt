package com.varpihovsky.core_repo.repo

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

import android.util.Log
import com.varpihovsky.core.Refreshable
import com.varpihovsky.core.exceptions.ExceptionEventManager
import com.varpihovsky.core_db.dao.ConfidentialDAO
import com.varpihovsky.core_db.dao.MessageDAO
import com.varpihovsky.core_db.dao.ProfileDAO
import com.varpihovsky.core_network.managers.JetIQMessageManager
import com.varpihovsky.repo_data.MessageDTO
import com.varpihovsky.repo_data.MessageToSendDTO
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
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
    fun loadMessages()

    /**
     * Returns flow of current messages in database. To load messages use [loadMessages] method.
     *
     * @return list of [MessageDTO]
     */
    fun getMessages(): Flow<List<MessageDTO>>

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

    companion object {
        operator fun invoke(
            jetIQMessageManager: JetIQMessageManager,
            messageDAO: MessageDAO,
            confidentialDAO: ConfidentialDAO,
            profileDAO: ProfileDAO,
            exceptionEventManager: ExceptionEventManager
        ): MessagesRepo = MessagesRepoImpl(
            jetIQMessageManager,
            messageDAO,
            confidentialDAO,
            profileDAO,
            exceptionEventManager
        )
    }
}

private class MessagesRepoImpl constructor(
    private val jetIQMessageManager: JetIQMessageManager,
    private val messageDAO: MessageDAO,
    confidentialDAO: ConfidentialDAO,
    profileDAO: ProfileDAO,
    exceptionEventManager: ExceptionEventManager
) : ConfidentRepo(confidentialDAO, profileDAO, exceptionEventManager), MessagesRepo {
    override val isLoading
        get() = _isLoading

    override fun onRefresh() {
        loadMessages()
    }

    private val _isLoading = mutableStateOf(false)

    override fun loadMessages() {
        repoScope.launch {
            _isLoading.value = true

            try {
                launchWithTimeout()
            } catch (e: TimeoutCancellationException) {
                Log.e("Application", Log.getStackTraceString(e))
            }

            delay(LOADING_DELAY)

            _isLoading.value = false
        }
    }

    private suspend fun launchWithTimeout() {
        withTimeout(LOADING_TIMEOUT) {
            processMessagesLoading()
        }
    }

    private suspend fun processMessagesLoading() {
        wrapException(
            result = jetIQMessageManager.getMessages(requireSession()),
            onSuccess = { addMessagesToDatabase(it.value) },
        )
    }

    private fun addMessagesToDatabase(messages: List<MessageDTO>) {
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

    companion object {
        private const val LOADING_DELAY = 500L
        private const val LOADING_TIMEOUT = 15000L
    }
}