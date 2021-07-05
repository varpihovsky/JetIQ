package com.varpihovsky.jetiq.back.api.managers

import com.varpihovsky.jetiq.back.api.JetIQApi
import com.varpihovsky.jetiq.back.api.JetIQManager
import com.varpihovsky.jetiq.back.dto.MarkbookSubjectDTO
import com.varpihovsky.jetiq.back.dto.SubjectDTO
import com.varpihovsky.jetiq.back.dto.relations.SubjectDetailsWithTasks
import com.varpihovsky.jetiq.back.dto.util.deserializeMarkbookSubjects
import com.varpihovsky.jetiq.back.dto.util.deserializeSubjectDetails
import com.varpihovsky.jetiq.back.dto.util.deserializeSubjectTasks
import com.varpihovsky.jetiq.back.dto.util.deserializeSubjects
import com.varpihovsky.jetiq.system.ConnectionManager
import javax.inject.Inject

class JetIQSubjectManager @Inject constructor(
    private val jetIQApi: JetIQApi,
    connectionManager: ConnectionManager
) : JetIQManager(connectionManager) {
    fun getSuccessJournal(session: String): List<SubjectDTO> {
        throwExceptionWhenNotConnected()

        val response = jetIQApi.getSuccessJournal(session).execute()

        throwExceptionWhenUnsuccessful(response, ERROR_MESSAGE)

        return deserializeSubjects(response.body()!!.string())
    }

    fun getSubjectDetails(session: String, cardId: Int): SubjectDetailsWithTasks {
        throwExceptionWhenNotConnected()

        val response = jetIQApi.getSubjectDetails(session, cardId).execute()

        throwExceptionWhenUnsuccessful(response, ERROR_MESSAGE)

        val str = response.body()!!.string()

        val subjectDetailsDTO = deserializeSubjectDetails(str)
        val subjectTasks = deserializeSubjectTasks(str)

        return SubjectDetailsWithTasks(subjectDetailsDTO, subjectTasks)
    }

    fun getMarkbookSubjects(session: String): List<MarkbookSubjectDTO> {
        throwExceptionWhenNotConnected()

        val response = jetIQApi.getMarkbookSubjects(session).execute()

        throwExceptionWhenUnsuccessful(response, ERROR_MESSAGE)

        return deserializeMarkbookSubjects(response.body()!!.string())
    }

    companion object {
        private const val ERROR_MESSAGE = "Критична помилка, зверніться до розробників"
    }
}