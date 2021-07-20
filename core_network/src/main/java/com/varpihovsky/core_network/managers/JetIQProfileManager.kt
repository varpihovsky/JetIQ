package com.varpihovsky.core_network.managers

import com.varpihovsky.core_network.JetIQApi
import com.varpihovsky.core_network.JetIQManager
import com.varpihovsky.core_network.result.EmptyResult
import com.varpihovsky.core_network.result.Result
import com.varpihovsky.core_network.result.asHttpResponse
import com.varpihovsky.repo_data.ProfileDTO

/**
 * Interface with which you can login user in server and get user-related data or logout user from server.
 *
 * @author Vladyslav Podrezenko
 */
interface JetIQProfileManager {
    /**
     * Returns [profile][ProfileDTO] of current user if everything went right.
     * If something went wrong returns null.
     *
     * @param login login of current user
     * @param password password of current user
     *
     * @return [ProfileDTO]
     */
    suspend fun login(login: String, password: String): Result<ProfileDTO?>

    /**
     * Deletes session in server.
     *
     * @param session session of current user.
     *
     * @return [EmptyResult]
     */
    suspend fun logout(session: String): EmptyResult

    companion object {
        operator fun invoke(jetIQApi: JetIQApi): JetIQProfileManager =
            JetIQProfileManagerImpl(jetIQApi)
    }
}

class JetIQProfileManagerImpl(private val jetIQApi: JetIQApi) : JetIQManager(),
    JetIQProfileManager {
    override suspend fun login(login: String, password: String): Result<ProfileDTO?> {
        return mapResult(jetIQApi.authorize(login, password)) {
            val session = it.asHttpResponse().headers.get("Set-Cookie")
            val nullableProfile = it.value
            val response = if (nullableProfile.d_id == null) {
                null
            } else {
                ProfileDTO(
                    nullableProfile.course_num!!,
                    nullableProfile.d_id,
                    nullableProfile.d_name!!,
                    null,
                    nullableProfile.email!!,
                    nullableProfile.f_id!!,
                    nullableProfile.gr_id!!,
                    nullableProfile.gr_name!!,
                    nullableProfile.id!!,
                    nullableProfile.photo_url!!,
                    session,
                    nullableProfile.spec_id!!,
                    nullableProfile.u_name!!
                )
            }
            Result.Success.Value(response)
        }
    }

    override suspend fun logout(session: String): EmptyResult {
        return jetIQApi.logout(session)
    }
}