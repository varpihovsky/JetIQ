package com.varpihovsky.core_db.dao

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

import com.varpihovsky.repo_data.MarkbookSubjectDTO
import com.varpihovsky.repo_data.SubjectDetailsDTO
import com.varpihovsky.repo_data.SubjectTaskDTO
import com.varpihovsky.repo_data.relations.SubjectDetailsWithTasks
import kotlinx.coroutines.flow.Flow

expect interface SubjectDetailsDAO {
    fun insertDetails(subjectDetailsDTO: SubjectDetailsDTO)

    fun insertTask(subjectTaskDTO: SubjectTaskDTO)

    fun insertMarkbookSubject(markbookSubjectDTO: MarkbookSubjectDTO)

    fun deleteAllDetails()

    fun deleteAllTasks()

    fun deleteAllMarkbookSubjects()

    fun getDetailsOnly(): Flow<List<SubjectDetailsDTO>>

    fun getDetailsById(id: Int): Flow<SubjectDetailsWithTasks>

    fun getMarkbookSubjects(): Flow<List<MarkbookSubjectDTO>>

    fun getMarkbookSubjectsList(): List<MarkbookSubjectDTO>

    fun getMarkbookSubjectById(id: Int): Flow<MarkbookSubjectDTO>
}