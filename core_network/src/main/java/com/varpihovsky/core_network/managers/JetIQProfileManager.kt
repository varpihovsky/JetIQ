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
            val response = if (nullableProfile.departmentId == null) {
                null
            } else {
                ProfileDTO(
                    nullableProfile.course!!,
                    nullableProfile.departmentId,
                    nullableProfile.departmentName!!,
                    null,
                    nullableProfile.email!!,
                    nullableProfile.facultyId!!,
                    nullableProfile.groupId!!,
                    nullableProfile.groupName!!,
                    nullableProfile.id!!,
                    nullableProfile.photoUrl!!,
                    session,
                    nullableProfile.specId!!,
                    nullableProfile.fullName!!
                )
            }
            Result.Success.Value(response)
        }
    }

    override suspend fun logout(session: String): EmptyResult {
        return jetIQApi.logout(session)
    }
}