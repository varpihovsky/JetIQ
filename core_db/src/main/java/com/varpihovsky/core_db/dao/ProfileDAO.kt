package com.varpihovsky.core_db.dao

/* JetIQ
 * Copyright © 2021 Vladyslav Podrezenko
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

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

    @Query("SELECT * FROM ProfileDTO LIMIT 1")
    fun getProfile(): ProfileDTO

    @Insert(onConflict = REPLACE)
    override fun set(t: ProfileDTO)

    @Query("DELETE FROM ProfileDTO")
    override fun delete()
}