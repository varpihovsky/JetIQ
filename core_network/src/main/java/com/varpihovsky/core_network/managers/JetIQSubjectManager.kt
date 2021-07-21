package com.varpihovsky.core_network.managers

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

import com.varpihovsky.core_network.JetIQApi
import com.varpihovsky.core_network.JetIQManager
import com.varpihovsky.core_network.parsers.deserializeMarkbookSubjects
import com.varpihovsky.core_network.parsers.deserializeSubjectDetails
import com.varpihovsky.core_network.parsers.deserializeSubjectTasks
import com.varpihovsky.core_network.parsers.deserializeSubjects
import com.varpihovsky.core_network.result.Result
import com.varpihovsky.repo_data.MarkbookSubjectDTO
import com.varpihovsky.repo_data.SubjectDTO
import com.varpihovsky.repo_data.relations.SubjectDetailsWithTasks

/**
 * Interface with which you can get information about current user's subjects.
 *
 * @author Vladyslav Podrezenko
 */
interface JetIQSubjectManager {
    /**
     * Returns list of subjects specified in success journal.
     *
     * @param session session of current user.
     *
     * @return list of [SubjectDTO]
     */
    suspend fun getSuccessJournal(session: String): Result<List<SubjectDTO>>

    /**
     * Returns details of subject which you got in [getSuccessJournal].
     *
     * @param session session of current user.
     * @param cardId id of subject got in [getSuccessJournal]
     *
     * @return [SubjectDetailsWithTasks]
     */
    suspend fun getSubjectDetails(session: String, cardId: Int): Result<SubjectDetailsWithTasks>

    /**
     * Returns list of subjects specified in markbook.
     *
     * @param session session of current user.
     *
     * @return list of [MarkbookSubjectDTO]
     */
    suspend fun getMarkbookSubjects(session: String): Result<List<MarkbookSubjectDTO>>

    companion object {
        operator fun invoke(jetIQApi: JetIQApi): JetIQSubjectManager =
            JetIQSubjectManagerImpl(jetIQApi)

    }
}

class JetIQSubjectManagerImpl(private val jetIQApi: JetIQApi) : JetIQManager(),
    JetIQSubjectManager {
    override suspend fun getSuccessJournal(session: String): Result<List<SubjectDTO>> {
        return mapResult(jetIQApi.getSuccessJournal(cookie = session)) {
            val subjects = deserializeSubjects(it.value.string())
            Result.Success.Value(subjects)
        }
    }

    override suspend fun getSubjectDetails(
        session: String,
        cardId: Int
    ): Result<SubjectDetailsWithTasks> {
        return mapResult(jetIQApi.getSubjectDetails(session, cardId)) {
            val json = it.value.string()

            Result.Success.Value(
                SubjectDetailsWithTasks(
                    deserializeSubjectDetails(json),
                    deserializeSubjectTasks(json)
                )
            )
        }
    }

    override suspend fun getMarkbookSubjects(session: String): Result<List<MarkbookSubjectDTO>> {
        return mapResult(jetIQApi.getMarkbookSubjects(session)) {
            Result.Success.Value(deserializeMarkbookSubjects(it.value.string()))
        }
    }
}