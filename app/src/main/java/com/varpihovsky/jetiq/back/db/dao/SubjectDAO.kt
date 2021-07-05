package com.varpihovsky.jetiq.back.db.dao

import androidx.room.*
import com.varpihovsky.jetiq.back.dto.SubjectDTO
import kotlinx.coroutines.flow.Flow

@Dao
interface SubjectDAO {
    @Query("SELECT * FROM SubjectDTO WHERE card_id=:id")
    fun getSubjectById(id: Int): Flow<SubjectDTO>

    @Query("SELECT * FROM SubjectDTO")
    fun getAllSubjects(): Flow<List<SubjectDTO>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(subjectDTO: SubjectDTO)

    @Delete
    fun delete(subjectDTO: SubjectDTO)

    @Query("DELETE FROM SubjectDTO WHERE card_id=:id")
    fun deleteSubjectById(id: Int)
}