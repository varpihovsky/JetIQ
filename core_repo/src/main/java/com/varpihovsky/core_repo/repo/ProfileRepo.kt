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
import javax.inject.Inject

interface ProfileRepo {
    suspend fun login(login: String, password: String): Boolean
    suspend fun logout()

    fun getProfile(): Flow<ProfileDTO>
    fun getConfidential(): Flow<Confidential>

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
        wrapException(jetIQProfileManager.logout())
    }

    override fun getProfile() = profileDAO.get()

    override fun getConfidential() = confidentialDAO.get()

    override fun clear() {
        profileDAO.delete()
        confidentialDAO.delete()
    }
}