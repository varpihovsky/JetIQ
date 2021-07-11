package com.varpihovsky.jetiq.screens.profile

import androidx.lifecycle.LiveData
import com.varpihovsky.jetiq.back.dto.MarkbookSubjectDTO
import com.varpihovsky.jetiq.back.dto.SubjectDTO
import com.varpihovsky.jetiq.back.dto.SubjectDetailsDTO
import com.varpihovsky.jetiq.back.model.ProfileModel
import com.varpihovsky.jetiq.back.model.SubjectModel
import com.varpihovsky.jetiq.system.ConnectionManager
import com.varpihovsky.jetiq.system.exceptions.Values
import com.varpihovsky.jetiq.system.util.CoroutineDispatchers
import com.varpihovsky.jetiq.ui.dto.MarksInfo
import com.varpihovsky.jetiq.ui.dto.UIProfileDTO
import com.varpihovsky.jetiq.ui.dto.UISubjectDTO
import com.varpihovsky.jetiq.ui.dto.formMarksInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class ProfileInteractor @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val profileModel: ProfileModel,
    private val subjectModel: SubjectModel,
    private val connectionManager: ConnectionManager
) {
    val isLoading: LiveData<Boolean>
        get() = subjectModel.isLoading

    private val scope = CoroutineScope(dispatchers.IO)

    lateinit var profileData: Flow<UIProfileDTO>

    lateinit var successData: Flow<Pair<List<MarksInfo>, List<UISubjectDTO>>>


    lateinit var markbookData: Flow<Pair<List<MarksInfo>, List<UISubjectDTO>>>

    init {
        runBlocking {
            scope.launch { startLoading() }
            scope.launch { initMappers() }.join()
        }
    }

    private fun startLoading() {
        subjectModel.loadSuccessJournal()
        subjectModel.loadMarkbookSubjects()
    }

    private fun initMappers() {
        initProfileMapper()
        initSubjectListMapper()
        initMarkbookMapper()
    }

    private fun initProfileMapper() {
        profileData = profileModel.getProfile().map { it.toUIDTO() }
    }

    private fun initSubjectListMapper() {
        successData = subjectModel.getSubjectList()
            .combine(subjectModel.getSubjectDetailsList()) { subjects, details ->
                Pair(
                    formSuccessMarksInfo(subjects, details),
                    formSuccessSubjects(subjects, details)
                )
            }
    }

    private fun initMarkbookMapper() {
        markbookData = subjectModel.getMarkbookSubjects().map {
            Pair(formMarkbookMarksInfo(it), formMarkbookSubjects(it))
        }
    }

    private fun formSuccessMarksInfo(
        subjects: List<SubjectDTO>,
        subjectDetails: List<SubjectDetailsDTO>
    ): List<MarksInfo> {
        val subjectDetailsMutable = subjectDetails.toMutableList()
        return formMarksInfo(
            array = subjects,
            semesterSelector = { it.sem.toInt() },
            gradeSelector = { subject -> subjectDetailsMutable.find { subject.card_id.toInt() == it.id }?.total },
            sortSelector = { it.sem }
        )
    }

    private fun formSuccessSubjects(
        subjects: List<SubjectDTO>,
        subjectDetails: List<SubjectDetailsDTO>
    ): MutableList<UISubjectDTO> {
        val uiSubjects = mutableListOf<UISubjectDTO>()
        val subjectDetailsMutable = subjectDetails.toMutableList()
        subjects.forEachIndexed { index, subject ->
            subjectDetailsMutable.find { subject.card_id.toInt() == it.id }?.let { details ->
                uiSubjects.add(subject.toUIDTO(details.total))
                subjectDetailsMutable.remove(details)
            }
        }
        return uiSubjects
    }

    private fun formMarkbookMarksInfo(markbookSubjects: List<MarkbookSubjectDTO>): List<MarksInfo> {
        return formMarksInfo(
            array = markbookSubjects,
            semesterSelector = { it.semester },
            gradeSelector = { it.total },
            sortSelector = { it.semester }
        )
    }

    private fun formMarkbookSubjects(markbookSubjects: List<MarkbookSubjectDTO>): List<UISubjectDTO> {
        return markbookSubjects.map { it.toUIDTO() }
    }

    suspend fun refresh() {
        if (!connectionManager.isConnected()) {
            throw RuntimeException(Values.INTERNET_UNAVAILABLE)
        }

        subjectModel.removeAllSubjects()

        startLoading()
    }
}