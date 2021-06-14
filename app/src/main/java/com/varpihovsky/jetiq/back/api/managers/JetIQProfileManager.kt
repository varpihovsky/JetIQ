package com.varpihovsky.jetiq.back.api.managers

import com.varpihovsky.jetiq.back.api.JetIQApi
import com.varpihovsky.jetiq.back.api.JetIQManager
import com.varpihovsky.jetiq.back.dto.ProfileDTO
import com.varpihovsky.jetiq.system.ConnectionManager
import javax.inject.Inject

class JetIQProfileManager @Inject constructor(
    private val jetIQApi: JetIQApi,
    connectionManager: ConnectionManager
) : JetIQManager(connectionManager) {
    fun login(login: String, password: String): ProfileDTO {
        throwExceptionWhenNotConnected()

        val response = jetIQApi.authorize(login, password).execute()

        throwExceptionWhenUnsuccessful(response, "Неправильний логін або пароль") {
            session == "wrong login or password"
        }

        return response.body()!!
    }
}