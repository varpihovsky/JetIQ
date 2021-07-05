package com.varpihovsky.jetiq.back.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.varpihovsky.jetiq.back.dto.ProfileDTO
import kotlinx.coroutines.flow.Flow

@Dao
interface ProfileDAO {
    @Query("SELECT * FROM ProfileDTO LIMIT 1")
    fun getProfile(): Flow<ProfileDTO>

    @Insert
    fun add(profileDTO: ProfileDTO)

    @Query("DELETE FROM ProfileDTO")
    fun deleteAll()

    fun insert(profileDTO: ProfileDTO) {
        deleteAll()
        add(profileDTO)
    }
}