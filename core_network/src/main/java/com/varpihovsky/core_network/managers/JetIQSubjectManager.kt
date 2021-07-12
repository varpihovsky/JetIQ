package com.varpihovsky.core_network.managers

import com.varpihovsky.core.ConnectionManager
import com.varpihovsky.core_network.JetIQApi
import com.varpihovsky.core_network.JetIQManager
import com.varpihovsky.core_network.parsers.deserializeMarkbookSubjects
import com.varpihovsky.core_network.parsers.deserializeSubjectDetails
import com.varpihovsky.core_network.parsers.deserializeSubjectTasks
import com.varpihovsky.core_network.parsers.deserializeSubjects
import com.varpihovsky.repo_data.MarkbookSubjectDTO
import com.varpihovsky.repo_data.SubjectDTO
import com.varpihovsky.repo_data.relations.SubjectDetailsWithTasks
import javax.inject.Inject

class JetIQSubjectManager @Inject constructor(
    private val jetIQApi: JetIQApi,
    connectionManager: ConnectionManager
) : JetIQManager(connectionManager) {
    fun getSuccessJournal(session: String): List<SubjectDTO> {
        throwExceptionWhenNotConnected()

        val response = jetIQApi.getSuccessJournal(session).execute()

        throwExceptionWhenUnsuccessful(response, STANDARD_ERROR_MESSAGE)

        return deserializeSubjects(response.body()!!.string())
    }

    fun getSubjectDetails(session: String, cardId: Int): SubjectDetailsWithTasks {
        val response = exceptionWrap { jetIQApi.getSubjectDetails(session, cardId).execute() }

        val str = response!!.string()

        val subjectDetailsDTO = deserializeSubjectDetails(str)
        val subjectTasks = deserializeSubjectTasks(str)

        return SubjectDetailsWithTasks(subjectDetailsDTO, subjectTasks)
    }

    fun getMarkbookSubjects(session: String): List<MarkbookSubjectDTO> {
        return deserializeMarkbookSubjects(exceptionWrap {
            jetIQApi.getMarkbookSubjects(session).execute()
        }!!.string())
    }
}