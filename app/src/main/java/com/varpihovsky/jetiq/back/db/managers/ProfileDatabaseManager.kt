package com.varpihovsky.jetiq.back.db.managers

import com.varpihovsky.jetiq.back.db.dao.ProfileDAO
import com.varpihovsky.jetiq.back.dto.ProfileDTO
import kotlinx.coroutines.flow.distinctUntilChanged
import javax.inject.Inject

class ProfileDatabaseManager @Inject constructor(private val profileDAO: ProfileDAO) {
    fun putProfile(profileDTO: ProfileDTO) {
        profileDAO.insert(profileDTO)
    }

    fun getProfile() = profileDAO.getProfile().distinctUntilChanged()
}