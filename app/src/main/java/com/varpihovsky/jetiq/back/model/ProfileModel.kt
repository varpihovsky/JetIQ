package com.varpihovsky.jetiq.back.model

import com.varpihovsky.jetiq.back.api.managers.JetIQProfileManager
import com.varpihovsky.jetiq.back.db.managers.ConfidentialDatabaseManager
import com.varpihovsky.jetiq.back.db.managers.ProfileDatabaseManager
import com.varpihovsky.jetiq.back.dto.Confidential
import javax.inject.Inject

class ProfileModel @Inject constructor(
    private val profileDatabaseManager: ProfileDatabaseManager,
    private val confidentialDatabaseManager: ConfidentialDatabaseManager,
    private val jetIQProfileManager: JetIQProfileManager
) {
    fun login(login: String, password: String) {
        val profile = jetIQProfileManager.login(login, password)
        confidentialDatabaseManager.putConfidential(Confidential(login, password))
        profileDatabaseManager.putProfile(profile)
    }

    fun getProfile() = profileDatabaseManager.getProfile()

    fun getConfidential() = confidentialDatabaseManager.getConfidential()
}