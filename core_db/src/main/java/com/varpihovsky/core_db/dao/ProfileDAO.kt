package com.varpihovsky.core_db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.varpihovsky.repo_data.ProfileDTO
import kotlinx.coroutines.flow.Flow

@Dao
interface ProfileDAO : SingleEntryDAO<ProfileDTO> {
    @Query("SELECT * FROM ProfileDTO LIMIT 1")
    override fun get(): Flow<ProfileDTO>

    @Insert(onConflict = REPLACE)
    override fun set(t: ProfileDTO)

    @Query("DELETE FROM ProfileDTO")
    override fun delete()
}