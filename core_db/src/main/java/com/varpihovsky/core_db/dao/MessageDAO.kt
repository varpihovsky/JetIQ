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