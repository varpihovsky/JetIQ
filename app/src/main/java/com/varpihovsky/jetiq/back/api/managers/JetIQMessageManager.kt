package com.varpihovsky.jetiq.back.api.managers

import com.varpihovsky.jetiq.back.api.JetIQApi
import com.varpihovsky.jetiq.back.api.JetIQManager
import com.varpihovsky.jetiq.back.dto.CSRF
import com.varpihovsky.jetiq.back.dto.MessageDTO
import com.varpihovsky.jetiq.back.dto.MessageToSendDTO
import com.varpihovsky.jetiq.system.ConnectionManager
import com.varpihovsky.jetiq.ui.dto.ReceiverType
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
                isTeacher = when (message.type) {
                    ReceiverType.TEACHER -> 1
                    ReceiverType.STUDENT -> 0
                },
                message = message.body,
                csrf = scrf.body
            ).execute()
        }
    }
}