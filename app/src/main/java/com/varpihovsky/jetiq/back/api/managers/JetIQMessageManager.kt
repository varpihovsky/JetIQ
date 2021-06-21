package com.varpihovsky.jetiq.back.api.managers

import com.varpihovsky.jetiq.back.api.JetIQApi
import com.varpihovsky.jetiq.back.api.JetIQManager
import com.varpihovsky.jetiq.back.dto.MessageDTO
import com.varpihovsky.jetiq.system.ConnectionManager
import javax.inject.Inject

class JetIQMessageManager @Inject constructor(
    private val jetIQApi: JetIQApi,
    connectionManager: ConnectionManager
) : JetIQManager(connectionManager) {
    fun getMessages(session: String): List<MessageDTO> {
        throwExceptionWhenNotConnected()

        val response = jetIQApi.getMessages(session).execute()

        throwExceptionWhenUnsuccessful(response, STANDARD_ERROR_MESSAGE)

        return response.body()!!
    }
}