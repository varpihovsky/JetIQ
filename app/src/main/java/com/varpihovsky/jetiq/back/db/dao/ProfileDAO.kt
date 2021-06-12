package com.varpihovsky.jetiq.back.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.varpihovsky.jetiq.back.dto.Profile
import kotlinx.coroutines.flow.Flow

@Dao
interface ProfileDAO {
    @Query("SELECT * FROM Profile LIMIT 1")
    fun getProfile(): Flow<Profile>

    @Insert
    fun add(profile: Profile)

    @Query("DELETE FROM Profile")
    fun deleteAll()

    fun insert(profile: Profile){
        deleteAll()
        add(profile)
    }
}