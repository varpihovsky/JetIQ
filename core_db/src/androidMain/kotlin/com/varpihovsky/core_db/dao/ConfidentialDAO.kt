package com.varpihovsky.core_db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.varpihovsky.repo_data.Confidential
import kotlinx.coroutines.flow.Flow

@Dao
actual interface ConfidentialDAO : SingleEntryDAO<Confidential> {
    @Query("SELECT * FROM Confidential LIMIT 1")
    actual override fun get(): Flow<Confidential>

    @Insert(onConflict = REPLACE)
    actual override fun set(t: Confidential)

    @Query("DELETE FROM Confidential")
    actual override fun delete()
}