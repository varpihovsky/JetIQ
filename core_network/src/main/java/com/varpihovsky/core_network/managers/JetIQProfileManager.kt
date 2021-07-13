package com.varpihovsky.core_network.managers

import com.varpihovsky.core_network.JetIQApi
import com.varpihovsky.core_network.JetIQManager
import com.varpihovsky.core_network.result.EmptyResult
import com.varpihovsky.core_network.result.Result
import com.varpihovsky.core_network.result.asHttpResponse
import com.varpihovsky.repo_data.ProfileDTO

interface JetIQProfileManager {
    suspend fun login(login: String, password: String): Result<ProfileDTO>

    suspend fun logout(): EmptyResult

    companion object {
        operator fun invoke(jetIQApi: JetIQApi): JetIQProfileManager =
            JetIQProfileManagerImpl(jetIQApi)
    }
}

class JetIQProfileManagerImpl(private val jetIQApi: JetIQApi) : JetIQManager(),
    JetIQProfileManager {
    override suspend fun login(login: String, password: String): Result<ProfileDTO> {
        return mapResult(jetIQApi.authorize(login, password)) {
            val session = it.asHttpResponse().headers.get("Set-Cookie")
            val profile = it.value
            Result.Success.Value(profile.copy(session = session))
        }
    }

    override suspend fun logout(): EmptyResult {
        return jetIQApi.logout()
    }
}