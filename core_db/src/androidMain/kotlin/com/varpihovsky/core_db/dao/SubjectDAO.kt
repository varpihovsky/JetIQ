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
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.IGNORE
import androidx.room.Query
import com.varpihovsky.repo_data.SubjectDTO
import kotlinx.coroutines.flow.Flow

@Dao
actual interface SubjectDAO {
    @Query("SELECT * FROM SubjectDTO WHERE card_id=:id")
    actual fun getSubjectById(id: String): Flow<SubjectDTO>

    @Query("SELECT * FROM SubjectDTO")
    actual fun getAllSubjects(): Flow<List<SubjectDTO>>

    @Query("SELECT * FROM SubjectDTO")
    actual fun getAllSubjectsList(): List<SubjectDTO>

    @Insert(onConflict = IGNORE)
    actual fun insert(subjectDTO: SubjectDTO)

    @Delete
    actual fun delete(subjectDTO: SubjectDTO)

    @Query("DELETE FROM SubjectDTO WHERE card_id=:id")
    actual fun deleteSubjectById(id: Int)

    @Query("DELETE FROM SubjectDTO")
    actual fun deleteAll()
}