package com.varpihovsky.core_network.managers

import com.varpihovsky.core.ConnectionManager
import com.varpihovsky.core_network.JetIQApi
import com.varpihovsky.core_network.JetIQManager
import com.varpihovsky.repo_data.CSRF
import com.varpihovsky.repo_data.MessageDTO
import com.varpihovsky.repo_data.MessageToSendDTO
import javax.inject.Inject

class JetIQMessageManager @Inject constructor(
    private val jetIQApi: JetIQApi,
    connectionManager: ConnectionManager
) : JetIQManager(connectionManager) {
    fun getMessages(session: String): List<MessageDTO> {
        return exceptionWrap { jetIQApi.getMessages(session).execute() }
    }

    fun getCsrf(session: String): CSRF {
        return exceptionWrap { jetIQApi.getCsrf(session).execute() }
    }

    fun sendMessage(session: String, scrf: CSRF, message: MessageToSendDTO) {
        exceptionWrap {
            jetIQApi.sendMessage(
                cookie = session,
                receiverId = message.id,
                isTeacher = message.type,
                message = message.body,
                csrf = scrf.body
            ).execute()
        }
    }
}