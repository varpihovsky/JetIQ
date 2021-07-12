package com.varpihovsky.core_network.managers

import com.varpihovsky.core.ConnectionManager
import com.varpihovsky.core_network.JetIQApi
import com.varpihovsky.core_network.JetIQManager
import com.varpihovsky.repo_data.ProfileDTO
import javax.inject.Inject

class JetIQProfileManager @Inject constructor(
    private val jetIQApi: JetIQApi,
    connectionManager: ConnectionManager
) : JetIQManager(connectionManager) {
    fun login(login: String, password: String): ProfileDTO {
        throwExceptionWhenNotConnected()

        val response = jetIQApi.authorize(login, password).execute()
        val session = response.headers().get("Set-Cookie")

        throwExceptionWhenUnsuccessful(response, "Неправильний логін або пароль") {
            session == "wrong login or password"
        }

        throwExceptionWhenNull(
            "Невідома помилка! Зверніться до розробників.",
            response.body(),
            session
        )

        return response.body()!!.copy(session = session!!)
    }

    fun logout() {
        exceptionWrap { jetIQApi.logout().execute() }
    }
}