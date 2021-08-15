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
package com.varpihovsky.feature_profile

import androidx.compose.runtime.State
import com.varpihovsky.core.Refreshable
import com.varpihovsky.core.coroutines.runBlocking
import com.varpihovsky.core.util.CoroutineDispatchers
import com.varpihovsky.core_repo.repo.ProfileRepo
import com.varpihovsky.core_repo.repo.SubjectRepo
import com.varpihovsky.jetiqApi.data.MarkbookSubject
import com.varpihovsky.jetiqApi.data.Subject
import com.varpihovsky.jetiqApi.data.SubjectDetails
import com.varpihovsky.ui_data.dto.MarksInfo
import com.varpihovsky.ui_data.dto.UIProfileDTO
import com.varpihovsky.ui_data.dto.UISubjectDTO
import com.varpihovsky.ui_data.dto.formMarksInfo
import com.varpihovsky.ui_data.mappers.toUIDTO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

class ProfileInteractor(
    private val dispatchers: CoroutineDispatchers,
    private val profileModel: ProfileRepo,
    private val subjectModel: SubjectRepo,
) : Refreshable {
    override val isLoading: State<Boolean>
        get() = subjectModel.isLoading

    private val scope = CoroutineScope(dispatchers.IO)

    lateinit var profileData: Flow<UIProfileDTO>

    lateinit var successData: Flow<Pair<List<MarksInfo>, List<UISubjectDTO>>>


    lateinit var markbookData: Flow<Pair<List<MarksInfo>, List<UISubjectDTO>>>

    init {
        runBlocking {
            scope.launch(dispatchers.IO) { startLoading() }
            scope.launch { initMappers() }.join()
        }
    }

    private suspend fun startLoading() {
        subjectModel.load()
    }

    private fun initMappers() {
        initProfileMapper()
        initSubjectListMapper()
        initMarkbookMapper()
    }

    private fun initProfileMapper() {
        profileData = profileModel.getProfile().mapNotNull { it?.toUIDTO() }
    }

    private fun initSubjectListMapper() {
        successData = subjectModel.getSubjects()
            .combine(flow = subjectModel.getSubjectsDetails()) { subjects, details ->
                Pair(
                    formSuccessMarksInfo(subjects, details),
                    formSuccessSubjects(subjects, details)
                )
            }
    }

    private fun initMarkbookMapper() {
        markbookData = subjectModel.getMarkbook().map {
            Pair(formMarkbookMarksInfo(it), formMarkbookSubjects(it))
        }
    }

    private fun formSuccessMarksInfo(
        subjects: List<Subject>,
        subjectDetails: List<SubjectDetails>
    ): List<MarksInfo> {
        val subjectDetailsMutable = subjectDetails.toMutableList()
        return formMarksInfo(
            array = subjects,
            semesterSelector = { it.semester.toInt() },
            gradeSelector = { subject -> subjectDetailsMutable.find { subject.subjectId.toInt() == it.id }?.totalHundredPointMark },
            sortSelector = { it.semester }
        )
    }

    private fun formSuccessSubjects(
        subjects: List<Subject>,
        subjectDetails: List<SubjectDetails>
    ): MutableList<UISubjectDTO> {
        val uiSubjects = mutableListOf<UISubjectDTO>()
        val subjectDetailsMutable = subjectDetails.toMutableList()
        subjects.forEach { subject ->
            subjectDetailsMutable.find { subject.subjectId.toInt() == it.id }?.let { details ->
                uiSubjects.add(subject.toUIDTO(details.totalHundredPointMark))
                subjectDetailsMutable.remove(details)
            }
        }
        return uiSubjects
    }

    private fun formMarkbookMarksInfo(markbookSubjects: List<MarkbookSubject>): List<MarksInfo> {
        return formMarksInfo(
            array = markbookSubjects,
            semesterSelector = { it.semester },
            gradeSelector = { it.total.roundToInt() },
            sortSelector = { it.semester }
        )
    }

    private fun formMarkbookSubjects(markbookSubjects: List<MarkbookSubject>): List<UISubjectDTO> {
        return markbookSubjects.map { it.toUIDTO() }
    }

    override fun onRefresh() {
        subjectModel.onRefresh()
    }
}