package com.varpihovsky.core_db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.IGNORE
import androidx.room.Query
import com.varpihovsky.repo_data.MarkbookSubjectDTO
import com.varpihovsky.repo_data.SubjectDetailsDTO
import com.varpihovsky.repo_data.SubjectTaskDTO
import kotlinx.coroutines.flow.Flow

@Dao
interface SubjectDetailsDAO {
    @Insert(onConflict = IGNORE)
    fun insertDetails(subjectDetailsDTO: SubjectDetailsDTO)

    @Insert(onConflict = IGNORE)
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

    @Query("SELECT * FROM MarkbookSubjectDTO")
    fun getMarkbookSubjects(): Flow<List<MarkbookSubjectDTO>>
}