package com.varpihovsky.core_repo.repo

import com.varpihovsky.core_db.dao.ConfidentialDAO
import com.varpihovsky.core_db.dao.ProfileDAO
import com.varpihovsky.core_db.dao.SingleEntryDAO
import com.varpihovsky.repo_data.Confidential
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

abstract class ConfidentRepo internal constructor(
    private val confidentialDAO: ConfidentialDAO,
    private val profileDAO: ProfileDAO
) : Repo() {
    private var confidential: Confidential? = null
    private var session: String? = null

    init {
        launchCollection()
    }

    private fun launchCollection() {
        modelScope.launch {
            launch { collectConfidential() }
            launch { collectSession() }
        }
    }

    private suspend fun collectConfidential() {
        collectSingleEntry(confidentialDAO) {
            confidential = it
        }
    }

    private suspend fun collectSession() {
        collectSingleEntry(profileDAO) {
            session = it.session
        }
    }

    private suspend fun <T> collectSingleEntry(
        singleEntryDAO: SingleEntryDAO<T>,
        collector: (T) -> Unit
    ) {
        singleEntryDAO.get().filterNotNull().collect { collector(it) }
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