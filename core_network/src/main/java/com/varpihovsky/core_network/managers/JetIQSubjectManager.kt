package com.varpihovsky.core_network.managers

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

interface JetIQSubjectManager {
    suspend fun getSuccessJournal(session: String): Result<List<SubjectDTO>>

    suspend fun getSubjectDetails(session: String, cardId: Int): Result<SubjectDetailsWithTasks>

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