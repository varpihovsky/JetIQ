package com.varpihovsky.core_db.dao

import androidx.room.*
import com.varpihovsky.repo_data.MessageDTO
import kotlinx.coroutines.flow.Flow

@Dao
interface MessageDAO {
    @Query("SELECT * FROM MessageDTO")
    fun getMessages(): Flow<List<MessageDTO>>

    @Query("SELECT * FROM MessageDTO WHERE msg_id=:id")
    fun getMessageById(id: Int): Flow<MessageDTO>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addMessage(messageDTO: MessageDTO)

    @Delete
    fun deleteMessage(messageDTO: MessageDTO)

    @Query("DELETE FROM MessageDTO")
    fun deleteAll()
}