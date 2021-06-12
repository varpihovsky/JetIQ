package com.varpihovsky.jetiq.back.db.managers

import com.varpihovsky.jetiq.back.db.dao.ProfileDAO
import com.varpihovsky.jetiq.back.dto.Profile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged

class ProfileDatabaseManager(private val profileDAO: ProfileDAO) {
    fun putProfile(profile: Profile){
        profileDAO.insert(profile)
    }

    fun getProfile() = profileDAO.getProfile().distinctUntilChanged()
}