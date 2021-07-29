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

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.IGNORE
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import androidx.room.Transaction
import com.varpihovsky.repo_data.MarkbookSubjectDTO
import com.varpihovsky.repo_data.SubjectDetailsDTO
import com.varpihovsky.repo_data.SubjectTaskDTO
import com.varpihovsky.repo_data.relations.SubjectDetailsWithTasks
import kotlinx.coroutines.flow.Flow

@Dao
interface SubjectDetailsDAO {
    @Insert(onConflict = IGNORE)
    fun insertDetails(subjectDetailsDTO: SubjectDetailsDTO)

    @Insert(onConflict = REPLACE)
    fun insertTask(subjectTaskDTO: SubjectTaskDTO)

    @Insert(onConflict = IGNORE)
    fun insertMarkbookSubject(markbookSubjectDTO: MarkbookSubjectDTO)

    @Query("DELETE FROM SubjectDetailsDTO")
    fun deleteAllDetails()

    @Query("DELETE FROM SubjectTaskDTO")
    fun deleteAllTasks()

    @Query("DELETE FROM MarkbookSubjectDTO")
    fun deleteAllMarkbookSubjects()

    @Query("SELECT * FROM SubjectDetailsDTO")
    fun getDetailsOnly(): Flow<List<SubjectDetailsDTO>>

    @Transaction
    @Query("SELECT * FROM SubjectDetailsDTO WHERE id=:id")
    fun getDetailsById(id: Int): Flow<SubjectDetailsWithTasks>

    @Query("SELECT * FROM MarkbookSubjectDTO")
    fun getMarkbookSubjects(): Flow<List<MarkbookSubjectDTO>>

    @Query("SELECT * FROM MarkbookSubjectDTO")
    fun getMarkbookSubjectsList(): List<MarkbookSubjectDTO>

    @Query("SELECT * FROM MarkbookSubjectDTO WHERE id=:id")
    fun getMarkbookSubjectById(id: Int): Flow<MarkbookSubjectDTO>
}