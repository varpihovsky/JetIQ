package com.varpihovsky.jetiq.screens.profile

import com.varpihovsky.jetiq.back.dto.SubjectDTO
import com.varpihovsky.jetiq.back.dto.SubjectDetailsDTO
import com.varpihovsky.jetiq.back.model.ProfileModel
import com.varpihovsky.jetiq.back.model.SubjectModel
import com.varpihovsky.jetiq.ui.dto.MarksInfo
import com.varpihovsky.jetiq.ui.dto.UIProfileDTO
import com.varpihovsky.jetiq.ui.dto.UISubjectDTO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

class ProfileInteractor @Inject constructor(
    private val profileModel: ProfileModel,
    private val subjectModel: SubjectModel
) {
    private var subscriber: Interactable? = null

    init {
        GlobalScope.launch(Dispatchers.IO) { collectProfileData() }
        GlobalScope.launch(Dispatchers.IO) {
            collectSubjectsList()
        }
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

        subjectList.combine(subjectDetails) { subjects, subjectDetails ->
            Pair(subjects, subjectDetails)
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
        var semester = 1
        var grade = 0
        var subIndex = 1

        val marksInfo = mutableListOf<MarksInfo>()
        subjects.sortedBy { it.sem }.forEachIndexed { index, subject ->
            if (semester != subject.sem.toInt() || subject == subjects.last()) {
                marksInfo.add(MarksInfo(semester, grade / subIndex))
                semester++
                grade = 0
                subIndex = 1
            }
            subjectDetailsMutable.find { subject.card_id.toInt() == it.id }?.let { details ->
                grade += details.total
                subIndex++
                subjectDetailsMutable.remove(details)
            }
        }

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
        String(name.split(" ").filter { it.length > 2 }.map { it.first().toUpperCase() }
            .toCharArray())

    fun subscribe(subscriber: Interactable) {
        this.subscriber = subscriber
    }

    interface Interactable {
        fun onProfileChange(uiProfileDTO: UIProfileDTO)
        fun onSuccessMarksInfoChange(successMarksInfo: List<MarksInfo>)
        fun onSuccessSubjectsChange(successSubjects: List<UISubjectDTO>)
    }
}