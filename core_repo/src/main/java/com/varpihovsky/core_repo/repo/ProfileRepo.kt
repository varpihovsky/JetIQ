package com.varpihovsky.core_repo.repo

import com.varpihovsky.core_db.dao.ConfidentialDAO
import com.varpihovsky.core_db.dao.ProfileDAO
import com.varpihovsky.core_db.dao.reset
import com.varpihovsky.core_network.managers.JetIQProfileManager
import com.varpihovsky.repo_data.Confidential
import com.varpihovsky.repo_data.ProfileDTO
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface ProfileRepo {
    fun login(login: String, password: String)
    fun logout()

    fun getProfile(): Flow<ProfileDTO>
    fun getConfidential(): Flow<Confidential>

    fun clear()

    companion object {
        internal operator fun invoke(
            profileDAO: ProfileDAO,
            confidentialDAO: ConfidentialDAO,
            jetIQProfileManager: JetIQProfileManager
        ): ProfileRepo = ProfileRepoImpl(
            profileDAO,
            confidentialDAO,
            jetIQProfileManager
        )
    }
}

private class ProfileRepoImpl @Inject constructor(
    private val profileDAO: ProfileDAO,
    private val confidentialDAO: ConfidentialDAO,
    private val jetIQProfileManager: JetIQProfileManager
) : Repo(), ProfileRepo {
    override fun login(login: String, password: String) {
        val profile = jetIQProfileManager.login(login, password)
        confidentialDAO.reset(Confidential(login, password))
        profileDAO.reset(profile)
    }

    override fun logout() {
        jetIQProfileManager.logout()
    }

    override fun getProfile() = profileDAO.get()

    override fun getConfidential() = confidentialDAO.get()

    override fun clear() {
        profileDAO.delete()
        confidentialDAO.delete()
    }
}