package com.varpihovsky.jetiq.back.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.IGNORE
import androidx.room.Query
import com.varpihovsky.jetiq.back.dto.ContactDTO
import kotlinx.coroutines.flow.Flow

@Dao
interface ContactDAO {
    @Query("SELECT * FROM ContactDTO")
    fun getContacts(): Flow<List<ContactDTO>>

    @Insert(onConflict = IGNORE)
    fun insert(contactDTO: ContactDTO)

    @Delete
    fun delete(contactDTO: ContactDTO)

    @Query("DELETE FROM ContactDTO")
    fun clear()
}