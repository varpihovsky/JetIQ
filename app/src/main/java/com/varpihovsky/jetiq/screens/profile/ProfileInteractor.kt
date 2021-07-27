package com.varpihovsky.jetiq.screens.profile

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

import androidx.compose.runtime.State
import com.varpihovsky.core.Refreshable
import com.varpihovsky.core.util.CoroutineDispatchers
import com.varpihovsky.core_repo.repo.ProfileRepo
import com.varpihovsky.core_repo.repo.SubjectRepo
import com.varpihovsky.repo_data.MarkbookSubjectDTO
import com.varpihovsky.repo_data.SubjectDTO
import com.varpihovsky.repo_data.SubjectDetailsDTO
import com.varpihovsky.ui_data.MarksInfo
import com.varpihovsky.ui_data.UIProfileDTO
import com.varpihovsky.ui_data.UISubjectDTO
import com.varpihovsky.ui_data.formMarksInfo
import com.varpihovsky.ui_data.mappers.toUIDTO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.lastOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class ProfileInteractor @Inject constructor(
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
        profileData = profileModel.getProfile().map { it.toUIDTO() }
    }

    private fun initSubjectListMapper() {
        successData = subjectModel.getSubjects()
            .combine(subjectModel.getSubjectsDetails()) { subjects, details ->
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

    suspend fun getMarkbookDTOById(id: Int): MarkbookSubjectDTO? {
        return subjectModel.getMarkbook().lastOrNull()?.find { it.id == id }
    }

    override fun onRefresh() {
        subjectModel.onRefresh()
    }
}