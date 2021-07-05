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
        return exceptionWrap { jetIQApi.getMessages(session).execute() }
    }
}