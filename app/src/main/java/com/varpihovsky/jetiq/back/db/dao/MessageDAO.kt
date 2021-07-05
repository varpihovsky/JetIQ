package com.varpihovsky.jetiq.back.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.varpihovsky.jetiq.back.dto.MessageDTO
import kotlinx.coroutines.flow.Flow


@Dao
interface MessageDAO {
    @Query("SELECT * FROM MessageDTO")
    fun getMessages(): Flow<List<MessageDTO>>

    @Query("SELECT * FROM MessageDTO WHERE msg_id=:id")
    fun getMessageById(id: Int): Flow<MessageDTO>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addMessage(messageDTO: MessageDTO)
}