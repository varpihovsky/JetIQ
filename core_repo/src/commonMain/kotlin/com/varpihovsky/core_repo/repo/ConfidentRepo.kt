/* JetIQ
 * Copyright © 2021 Vladyslav Podrezenko
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.varpihovsky.core_repo.repo

import com.varpihovsky.core.exceptions.ExceptionEventManager
import com.varpihovsky.core_db.dao.ConfidentialDAO
import com.varpihovsky.core_db.dao.ProfileDAO
import com.varpihovsky.core_db.dao.SingleEntryDAO
import com.varpihovsky.repo_data.Confidential
import kotlinx.coroutines.flow.collect
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
        repoScope.launch {
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
            session = it?.session
        }
    }

    private suspend fun <T> collectSingleEntry(
        singleEntryDAO: SingleEntryDAO<T>,
        collector: (T?) -> Unit
    ) {
        singleEntryDAO.get().collect { collector(it) }
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