package com.varpihovsky.jetiq.back.db.dao

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import com.varpihovsky.jetiq.back.dto.SubjectDetailsDTO
import com.varpihovsky.jetiq.back.dto.SubjectTaskDTO
import com.varpihovsky.jetiq.back.dto.relations.SubjectDetailsWithTasks
import kotlinx.coroutines.flow.Flow

@Dao
interface SubjectDetailsDAO {
    @Insert(onConflict = REPLACE)
    fun insertDetails(subjectDetailsDTO: SubjectDetailsDTO)

    @Insert(onConflict = REPLACE)
    fun insertTask(subjectTaskDTO: SubjectTaskDTO)

    @Delete
    fun deleteDetails(subjectDetailsDTO: SubjectDetailsDTO)

    @Delete
    fun deleteTask(subjectTaskDTO: SubjectTaskDTO)

    @Query("SELECT * FROM SubjectDetailsDTO")
    fun getDetailsOnly(): Flow<List<SubjectDetailsDTO>>

    @Query("SELECT * FROM SubjectDetailsDTO WHERE id=:id")
    fun getDetailsById(id: Int): Flow<SubjectDetailsDTO>

    @Transaction
    @Query("SELECT * FROM SubjectDetailsDTO WHERE id=:id")
    fun getDetailsAndTasksByDetailsId(id: Int): Flow<SubjectDetailsWithTasks>

    @Transaction
    @Query("SELECT * FROM SubjectDetailsDTO")
    fun getDetailsWithTasks(): Flow<List<SubjectDetailsWithTasks>>
}