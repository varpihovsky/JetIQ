package com.varpihovsky.jetiq.back.db.dao

import androidx.room.*
import androidx.room.OnConflictStrategy.IGNORE
import com.varpihovsky.jetiq.back.dto.MarkbookSubjectDTO
import com.varpihovsky.jetiq.back.dto.SubjectDetailsDTO
import com.varpihovsky.jetiq.back.dto.SubjectTaskDTO
import com.varpihovsky.jetiq.back.dto.relations.SubjectDetailsWithTasks
import kotlinx.coroutines.flow.Flow

@Dao
interface SubjectDetailsDAO {
    @Insert(onConflict = IGNORE)
    fun insertDetails(subjectDetailsDTO: SubjectDetailsDTO)

    @Insert(onConflict = IGNORE)
    fun insertTask(subjectTaskDTO: SubjectTaskDTO)

    @Insert(onConflict = IGNORE)
    fun insertMarkbookSubject(markbookSubjectDTO: MarkbookSubjectDTO)

    @Delete
    fun deleteDetails(subjectDetailsDTO: SubjectDetailsDTO)

    @Query("DELETE FROM SubjectDetailsDTO")
    fun deleteAllDetails()

    @Delete
    fun deleteTask(subjectTaskDTO: SubjectTaskDTO)

    @Query("DELETE FROM SubjectTaskDTO")
    fun deleteAllTasks()

    @Delete
    fun deleteMarkbookSubject(markbookSubjectDTO: MarkbookSubjectDTO)

    @Query("DELETE FROM MarkbookSubjectDTO")
    fun deleteAllMarkbookSubjects()

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

    @Query("SELECT * FROM MarkbookSubjectDTO")
    fun getMarkbookSubjects(): Flow<List<MarkbookSubjectDTO>>

    @Query("SELECT * FROM MarkbookSubjectDTO WHERE id=:id")
    fun getMarkbookSubjectById(id: Int): Flow<MarkbookSubjectDTO>
}