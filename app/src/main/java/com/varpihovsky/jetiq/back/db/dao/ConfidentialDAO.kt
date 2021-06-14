package com.varpihovsky.jetiq.back.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.varpihovsky.jetiq.back.dto.Confidential
import kotlinx.coroutines.flow.Flow

@Dao
interface ConfidentialDAO {
    @Query("SELECT * FROM Confidential LIMIT 1")
    fun getConfidential(): Flow<Confidential>

    @Insert
    fun add(confidential: Confidential)

    @Query("DELETE FROM Confidential")
    fun deleteAll()

    fun insert(confidential: Confidential) {
        deleteAll()
        add(confidential)
    }
}