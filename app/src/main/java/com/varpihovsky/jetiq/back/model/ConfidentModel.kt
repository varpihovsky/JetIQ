package com.varpihovsky.jetiq.back.model

import com.varpihovsky.jetiq.back.db.managers.ConfidentialDatabaseManager
import com.varpihovsky.jetiq.back.db.managers.ProfileDatabaseManager
import com.varpihovsky.jetiq.back.dto.Confidential
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

abstract class ConfidentModel constructor(
    private val confidentialDatabaseManager: ConfidentialDatabaseManager,
    private val profileDatabaseManager: ProfileDatabaseManager
) {
    private var confidential: Confidential? = null
    private var session: String? = null

    init {
        GlobalScope.launch(Dispatchers.IO) { collectConfidential() }
        GlobalScope.launch(Dispatchers.IO) { collectSession() }
    }

    private suspend fun collectConfidential() {
        confidentialDatabaseManager.getConfidential().collect {
            confidential = it
        }
    }

    private suspend fun collectSession() {
        profileDatabaseManager.getProfile().collect {
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