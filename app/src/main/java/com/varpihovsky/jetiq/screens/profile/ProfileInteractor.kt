package com.varpihovsky.jetiq.screens.profile

import androidx.lifecycle.LiveData
import com.varpihovsky.jetiq.back.dto.MarkbookSubjectDTO
import com.varpihovsky.jetiq.back.dto.SubjectDTO
import com.varpihovsky.jetiq.back.dto.SubjectDetailsDTO
import com.varpihovsky.jetiq.back.model.ProfileModel
import com.varpihovsky.jetiq.back.model.SubjectModel
import com.varpihovsky.jetiq.system.ConnectionManager
import com.varpihovsky.jetiq.system.util.ReactiveTask
import com.varpihovsky.jetiq.ui.dto.MarksInfo
import com.varpihovsky.jetiq.ui.dto.UIProfileDTO
import com.varpihovsky.jetiq.ui.dto.UISubjectDTO
import com.varpihovsky.jetiq.ui.dto.formMarksInfo
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class ProfileInteractor @Inject constructor(
    private val profileModel: ProfileModel,
    private val subjectModel: SubjectModel,
    private val connectionManager: ConnectionManager
) {
    val isLoading: LiveData<Boolean>
        get() = subjectModel.isLoading

    private var subscriber: Interactable? = null

    private val profileTask = ReactiveTask(task = this::collectProfileData)
    private val subjectListTask = ReactiveTask(task = this::collectSubjectsList)
    private val markbookSubjectsTask = ReactiveTask(task = this::collectMarkbookSubjects)

    init {
        profileTask.start()
        subjectListTask.start()
        markbookSubjectsTask.start()
    }

    private suspend fun collectProfileData() {
        profileModel.getProfile().collect {
            subscriber?.onProfileChange(
                UIProfileDTO(
                    it.id.toInt(),
                    getUsername(it.u_name),
                    cutFacultyName(it.d_name),
                    it.course_num,
                    it.gr_name,
                    0,
                    it.photo_url
                )
            )
        }
    }

    private fun getUsername(name: String): String {
        val strings = name.split(" ").subList(0, 2)
        return "${strings[0]} ${strings[1]}"
    }

    private suspend fun collectSubjectsList() {
        val subjectList = subjectModel.getSubjectList()
        val subjectDetails = subjectModel.getSubjectDetailsList()

        subjectList.combine(subjectDetails) { subjects, details ->
            Pair(subjects, details)
        }.collect {
            formSuccessMarksInfo(it.first, it.second)
            formSuccessSubjects(it.first, it.second)
        }
    }

    private fun formSuccessMarksInfo(
        subjects: List<SubjectDTO>,
        subjectDetails: List<SubjectDetailsDTO>
    ) {
        val subjectDetailsMutable = subjectDetails.toMutableList()
        val marksInfo = formMarksInfo(
            array = subjects,
            semesterSelector = { it.sem.toInt() },
            gradeSelector = { subject -> subjectDetailsMutable.find { subject.card_id.toInt() == it.id }?.total },
            sortSelector = { it.sem }
        )
        subscriber?.onSuccessMarksInfoChange(marksInfo)
    }

    private fun formSuccessSubjects(
        subjects: List<SubjectDTO>,
        subjectDetails: List<SubjectDetailsDTO>
    ) {
        val uiSubjects = mutableListOf<UISubjectDTO>()
        val subjectDetailsMutable = subjectDetails.toMutableList()
        subjects.forEachIndexed { index, subject ->
            subjectDetailsMutable.find { subject.card_id.toInt() == it.id }?.let { details ->
                uiSubjects.add(
                    UISubjectDTO(
                        subject.card_id.toInt(),
                        subject.subject,
                        subject.t_name,
                        details.total,
                        subject.sem.toInt()
                    )
                )
                subjectDetailsMutable.remove(details)
            }
        }

        subscriber?.onSuccessSubjectsChange(uiSubjects)
    }

    private fun cutFacultyName(name: String) =
        String(name.split(" ").filter { it.length > 2 }.map { it.first().uppercaseChar() }
            .toCharArray())

    private suspend fun collectMarkbookSubjects() {
        subjectModel.getMarkbookSubjects().collect { markbookSubjects ->
            formMarkbookMarksInfo(markbookSubjects)
            formMarkbookSubjects(markbookSubjects)
        }
    }

    private fun formMarkbookMarksInfo(markbookSubjects: List<MarkbookSubjectDTO>) {
        val marksInfo = formMarksInfo(
            array = markbookSubjects,
            semesterSelector = { it.semester },
            gradeSelector = { it.total },
            sortSelector = { it.semester }
        )
        subscriber?.onMarkbookMarksInfoChange(marksInfo)
    }

    private fun formMarkbookSubjects(markbookSubjects: List<MarkbookSubjectDTO>) {
        subscriber?.onMarkbookSubjectsChange(markbookSubjects.map {
            UISubjectDTO(
                it.id,
                it.subj_name,
                it.teacher,
                it.total,
                it.semester
            )
        })
    }

    fun subscribe(subscriber: Interactable) {
        this.subscriber = subscriber
    }

    suspend fun refresh() {
        if (!connectionManager.isConnected()) {
            throw RuntimeException("Немає підключення до інтернету, спробуйте ще раз")
        }

        subjectListTask.stop()
        markbookSubjectsTask.stop()

        subjectModel.removeAllSubjects()

        subjectListTask.start()
        markbookSubjectsTask.start()
    }

    interface Interactable {
        fun onProfileChange(uiProfileDTO: UIProfileDTO)
        fun onSuccessMarksInfoChange(successMarksInfo: List<MarksInfo>)
        fun onSuccessSubjectsChange(successSubjects: List<UISubjectDTO>)
        fun onMarkbookMarksInfoChange(markbookMarksInfo: List<MarksInfo>)
        fun onMarkbookSubjectsChange(markbookSubjects: List<UISubjectDTO>)
    }
}