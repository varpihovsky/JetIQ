package com.varpihovsky.core_network.managers

import com.varpihovsky.core_network.JetIQApi
import com.varpihovsky.core_network.JetIQManager
import com.varpihovsky.core_network.result.EmptyResult
import com.varpihovsky.core_network.result.Result
import com.varpihovsky.repo_data.CSRF
import com.varpihovsky.repo_data.MessageDTO
import com.varpihovsky.repo_data.MessageToSendDTO

interface JetIQMessageManager {
    suspend fun getMessages(session: String): Result<List<MessageDTO>>

    suspend fun getCsrf(session: String): Result<CSRF>

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