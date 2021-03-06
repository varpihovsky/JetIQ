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
package com.varpihovsky.core_repo.repo

import com.varpihovsky.core.exceptions.ExceptionEventManager
import com.varpihovsky.core_db.dao.ConfidentialDAO
import com.varpihovsky.core_db.dao.ProfileDAO
import com.varpihovsky.core_db.dao.reset
import com.varpihovsky.jetiqApi.Api
import com.varpihovsky.jetiqApi.data.Profile
import com.varpihovsky.repo_data.Confidential
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.last

/**
 * Interface which used for authorization on server and saving all user-related data.
 *
 * @author Vladyslav Podrezenko
 */
interface ProfileRepo {
    /**
     * Adds received user-related data into database and returns true if user successfully authorized.
     * If something went wrong returns false. Every successful call of function resets all user-related
     * data to a new one.
     *
     * @param login login of current user.
     * @param password password of current user.
     *
     * @return signal about operation status.
     */
    suspend fun login(login: String, password: String): Boolean

    /**
     * Deletes current information in server-side only.
     */
    suspend fun logout()

    /**
     * Returns current user flow.
     *
     * @return flow of [Profile]
     */
    fun getProfile(): Flow<Profile?>

    fun getProfileDTO(): Profile?

    /**
     * Returns current user flow of confidential.
     *
     * @return flow of [Confidential]
     */
    fun getConfidential(): Flow<Confidential?>

    /**
     * Clears all user-related data.
     */
    fun clear()

    companion object {
        operator fun invoke(
            profileDAO: ProfileDAO,
            confidentialDAO: ConfidentialDAO,
            api: Api,
            exceptionEventManager: ExceptionEventManager
        ): ProfileRepo = ProfileRepoImpl(
            profileDAO,
            confidentialDAO,
            api,
            exceptionEventManager
        )
    }
}

private class ProfileRepoImpl constructor(
    private val profileDAO: ProfileDAO,
    private val confidentialDAO: ConfidentialDAO,
    private val api: Api,
    exceptionEventManager: ExceptionEventManager
) : Repo(exceptionEventManager), ProfileRepo {
    override suspend fun login(login: String, password: String): Boolean {
        val profile = wrapException(
            result = api.authorize(login, password),
            onSuccess = { it.value },
            onFailure = { return false }
        )
        profile.let {
            confidentialDAO.reset(Confidential(login, password))
            profileDAO.reset(it)
        }

        return true
    }

    override suspend fun logout() {
        profileDAO.get().last()?.session?.let {
            wrapException(api.logout(it))
        }
    }

    override fun getProfile() = profileDAO.get()

    override fun getProfileDTO() = profileDAO.getProfile()

    override fun getConfidential() = confidentialDAO.get()

    override fun clear() {
        profileDAO.delete()
        confidentialDAO.delete()
    }
}