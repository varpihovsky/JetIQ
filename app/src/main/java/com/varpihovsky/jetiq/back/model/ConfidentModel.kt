package com.varpihovsky.jetiq.back.model

import com.varpihovsky.jetiq.back.db.managers.ConfidentialDatabaseManager
import com.varpihovsky.jetiq.back.db.managers.ProfileDatabaseManager
import com.varpihovsky.jetiq.back.dto.Confidential
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

abstract class ConfidentModel constructor(
    private val confidentialDatabaseManager: ConfidentialDatabaseManager,
    private val profileDatabaseManager: ProfileDatabaseManager
) : Model() {
    private var confidential: Confidential? = null
    private var session: String? = null

    init {
        modelScope.launch { collectConfidential() }
        modelScope.launch { collectSession() }
    }

    private suspend fun collectConfidential() {
        confidentialDatabaseManager.getConfidential().filterNotNull().collect {
            confidential = it
        }
    }

    private suspend fun collectSession() {
        profileDatabaseManager.getProfile().filterNotNull().collect {
            session = it.session
        }
    }

    protected fun requireConfidential(): Confidential {
        while (confidential == null);
        return confidential!!
    }

    protected fun requireSession(): String {
        while (session == null);
        return session!!
    }
}