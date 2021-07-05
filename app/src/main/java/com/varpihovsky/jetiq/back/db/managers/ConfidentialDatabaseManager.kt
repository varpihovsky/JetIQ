package com.varpihovsky.jetiq.back.db.managers

import com.varpihovsky.jetiq.back.db.dao.ConfidentialDAO
import com.varpihovsky.jetiq.back.dto.Confidential
import kotlinx.coroutines.flow.distinctUntilChanged
import javax.inject.Inject

class ConfidentialDatabaseManager @Inject constructor(private val confidentialDAO: ConfidentialDAO) {
    fun putConfidential(confidential: Confidential) {
        confidentialDAO.insert(confidential)
    }

    fun getConfidential() = confidentialDAO.getConfidential().distinctUntilChanged()

    fun removeConfidential() {
        confidentialDAO.deleteAll()
    }
}