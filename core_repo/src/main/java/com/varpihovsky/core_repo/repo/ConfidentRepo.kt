package com.varpihovsky.core_repo.repo

import com.varpihovsky.core.exceptions.ExceptionEventManager
import com.varpihovsky.core_db.dao.ConfidentialDAO
import com.varpihovsky.core_db.dao.ProfileDAO
import com.varpihovsky.core_db.dao.SingleEntryDAO
import com.varpihovsky.repo_data.Confidential
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

/**
 * Standard class for all Repo classes which need data about current user.
 *
 * @author Vladyslav Podrezenko
 */
abstract class ConfidentRepo internal constructor(
    private val confidentialDAO: ConfidentialDAO,
    private val profileDAO: ProfileDAO,
    exceptionEventManager: ExceptionEventManager
) : Repo(exceptionEventManager) {
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

    /**
     * Returns [Confidential] if it there is in repo. If it isn't causes thread block.
     *
     * @return [Confidential]
     */
    protected fun requireConfidential(): Confidential {
        while (confidential == null);
        return confidential!!
    }

    /**
     * Returns session if there is in repo. If it isn't causes thread block.
     *
     * @return session
     */
    protected fun requireSession(): String {
        while (session == null);
        return session!!
    }
}