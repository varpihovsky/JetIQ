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
package com.varpihovsky.core_db.dao

import com.varpihovsky.core_db.internal.delete
import com.varpihovsky.core_db.internal.get
import com.varpihovsky.core_db.internal.keyById
import com.varpihovsky.core_db.internal.putSingle
import com.varpihovsky.core_db.internal.types.ProfileInternal
import com.varpihovsky.core_db.internal.types.mappers.toExternal
import com.varpihovsky.core_db.internal.types.mappers.toInternal
import com.varpihovsky.jetiqApi.data.Profile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.kodein.db.DB
import org.kodein.db.flowOf

interface ProfileDAO : SingleEntryDAO<Profile> {
    override fun get(): Flow<Profile?>

    fun getProfile(): Profile?

    override fun set(t: Profile)

    override fun delete()

    companion object {
        operator fun invoke(db: DB): ProfileDAO = ProfileDAOImpl(db)
    }
}

private class ProfileDAOImpl(private val db: DB) : ProfileDAO {
    override fun get(): Flow<Profile?> {
        return db.flowOf<ProfileInternal>(db.keyById()).map { it?.toExternal() }
    }

    override fun getProfile(): Profile? {
        return db.get<ProfileInternal>()?.toExternal()
    }

    override fun set(t: Profile) {
        db.putSingle(t.toInternal())
    }

    override fun delete() {
        db.delete<ProfileInternal>()
    }

}