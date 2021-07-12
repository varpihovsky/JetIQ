package com.varpihovsky.core_db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.IGNORE
import androidx.room.Query
import com.varpihovsky.repo_data.SubjectDTO
import kotlinx.coroutines.flow.Flow

@Dao
interface SubjectDAO {
    @Query("SELECT * FROM SubjectDTO WHERE card_id=:id")
    fun getSubjectById(id: Int): Flow<SubjectDTO>

    @Query("SELECT * FROM SubjectDTO")
    fun getAllSubjects(): Flow<List<SubjectDTO>>

    @Query("SELECT * FROM SubjectDTO")
    fun getAllSubjectsList(): List<SubjectDTO>

    @Insert(onConflict = IGNORE)
    fun insert(subjectDTO: SubjectDTO)

    @Delete
    fun delete(subjectDTO: SubjectDTO)

    @Query("DELETE FROM SubjectDTO WHERE card_id=:id")
    fun deleteSubjectById(id: Int)

    @Query("DELETE FROM SubjectDTO")
    fun deleteAll()
}