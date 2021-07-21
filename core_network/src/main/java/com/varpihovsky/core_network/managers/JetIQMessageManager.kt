package com.varpihovsky.core_network.managers

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

import com.varpihovsky.core_network.JetIQApi
import com.varpihovsky.core_network.JetIQManager
import com.varpihovsky.core_network.result.EmptyResult
import com.varpihovsky.core_network.result.Result
import com.varpihovsky.repo_data.CSRF
import com.varpihovsky.repo_data.MessageDTO
import com.varpihovsky.repo_data.MessageToSendDTO

/**
 * Interface with which you can send or receive messages.
 *
 * @author Vladyslav Podrezenko
 */
interface JetIQMessageManager {
    /**
     * Returns list of messages.
     *
     * @param session session of current user.
     *
     * @return list of [MessageDTO]
     */
    suspend fun getMessages(session: String): Result<List<MessageDTO>>

    /**
     * Returns [csrf][CSRF] that is needed for message sending.
     *
     * @param session session of current user.
     *
     * @return [csrf][CSRF]
     */
    suspend fun getCsrf(session: String): Result<CSRF>

    /**
     * Sends message to receiver specified in [message class][MessageToSendDTO]. To send need to
     * get [csrf][CSRF].
     *
     * @param session session of current user
     * @param csrf parameter needed to message sending by server.
     * @param message message which should contain all field needed for sending.
     *
     * @return [EmptyResult]
     */
    suspend fun sendMessage(session: String, csrf: CSRF, message: MessageToSendDTO): EmptyResult

    companion object {
        operator fun invoke(jetIQApi: JetIQApi): JetIQMessageManager =
            JetIQMessageManagerImpl(jetIQApi)
    }
}

class JetIQMessageManagerImpl(private val jetIQApi: JetIQApi) : JetIQManager(),
    JetIQMessageManager {
    override suspend fun getMessages(session: String): Result<List<MessageDTO>> {
        return jetIQApi.getMessages(cookie = session)
    }

    override suspend fun getCsrf(session: String): Result<CSRF> {
        return jetIQApi.getCsrf(cookie = session)
    }

    override suspend fun sendMessage(
        session: String,
        csrf: CSRF,
        message: MessageToSendDTO
    ): EmptyResult {
        return jetIQApi.sendMessage(
            cookie = session,
            receiverId = message.id,
            isTeacher = message.type,
            message = message.body,
            csrf = csrf.body
        )
    }
}