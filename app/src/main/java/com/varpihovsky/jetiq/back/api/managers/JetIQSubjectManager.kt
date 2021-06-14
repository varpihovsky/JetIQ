package com.varpihovsky.jetiq.back.api.managers

import com.varpihovsky.jetiq.back.api.JetIQApi
import com.varpihovsky.jetiq.back.api.JetIQManager
import com.varpihovsky.jetiq.back.dto.SubjectDTO
import com.varpihovsky.jetiq.back.dto.deserializeSubjectDetails
import com.varpihovsky.jetiq.back.dto.deserializeSubjectTasks
import com.varpihovsky.jetiq.back.dto.deserializeSubjects
import com.varpihovsky.jetiq.back.dto.relations.SubjectDetailsWithTasks
import com.varpihovsky.jetiq.system.ConnectionManager
import javax.inject.Inject

class JetIQSubjectManager @Inject constructor(
    private val jetIQApi: JetIQApi,
    connectionManager: ConnectionManager
) : JetIQManager(connectionManager) {
    fun getSuccessJournal(login: String, password: String): List<SubjectDTO> {
        throwExceptionWhenNotConnected()

        val response = jetIQApi.getSuccessJournal(login, password).execute()

        throwExceptionWhenUnsuccessful(response, ERROR_MESSAGE)

        return deserializeSubjects(response.body()!!.string())
    }

    fun getSubjectDetails(login: String, password: String, cardId: Int): SubjectDetailsWithTasks {
        throwExceptionWhenNotConnected()

        val response = jetIQApi.getSubjectDetails(login, password, cardId).execute()

        throwExceptionWhenUnsuccessful(response, ERROR_MESSAGE)

        val str = response.body()!!.string()

        val subjectDetailsDTO = deserializeSubjectDetails(str)
        val subjectTasks = deserializeSubjectTasks(str)

        return SubjectDetailsWithTasks(subjectDetailsDTO, subjectTasks)
    }

    companion object {
        private const val ERROR_MESSAGE = "Критична помилка, зверніться до розробників"
    }
}