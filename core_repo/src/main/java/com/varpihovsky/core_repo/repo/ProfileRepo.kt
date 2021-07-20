package com.varpihovsky.core_repo.repo

import com.varpihovsky.core.exceptions.ExceptionEventManager
import com.varpihovsky.core.exceptions.Values
import com.varpihovsky.core_db.dao.ConfidentialDAO
import com.varpihovsky.core_db.dao.ProfileDAO
import com.varpihovsky.core_db.dao.reset
import com.varpihovsky.core_network.managers.JetIQProfileManager
import com.varpihovsky.repo_data.Confidential
import com.varpihovsky.repo_data.ProfileDTO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.last
import javax.inject.Inject

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
     * @return flow of [ProfileDTO]
     */
    fun getProfile(): Flow<ProfileDTO>

    /**
     * Returns current user flow of confidential.
     *
     * @return flow of [Confidential]
     */
    fun getConfidential(): Flow<Confidential>

    /**
     * Clears all user-related data.
     */
    fun clear()

    companion object {
        operator fun invoke(
            profileDAO: ProfileDAO,
            confidentialDAO: ConfidentialDAO,
            jetIQProfileManager: JetIQProfileManager,
            exceptionEventManager: ExceptionEventManager
        ): ProfileRepo = ProfileRepoImpl(
            profileDAO,
            confidentialDAO,
            jetIQProfileManager,
            exceptionEventManager
        )
    }
}

private class ProfileRepoImpl @Inject constructor(
    private val profileDAO: ProfileDAO,
    private val confidentialDAO: ConfidentialDAO,
    private val jetIQProfileManager: JetIQProfileManager,
    exceptionEventManager: ExceptionEventManager
) : Repo(exceptionEventManager), ProfileRepo {
    override suspend fun login(login: String, password: String): Boolean {
        val profile = wrapException(
            result = jetIQProfileManager.login(login, password),
            onSuccess = { it.value },
            onFailure = { return false }
        )
        profile?.let {
            confidentialDAO.reset(Confidential(login, password))
            profileDAO.reset(it)
        }

        if (profile == null) {
            exceptionEventManager.pushException(RuntimeException(Values.LOGIN_OR_PASS_IS_NOT_RIGHT))
        }

        return profile != null
    }

    override suspend fun logout() {
        profileDAO.get().last().session?.let {
            wrapException(jetIQProfileManager.logout(it))
        }
    }

    override fun getProfile() = profileDAO.get()

    override fun getConfidential() = confidentialDAO.get()

    override fun clear() {
        profileDAO.delete()
        confidentialDAO.delete()
    }
}