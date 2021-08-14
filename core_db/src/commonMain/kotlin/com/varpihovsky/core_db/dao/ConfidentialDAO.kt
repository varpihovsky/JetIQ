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

import com.varpihovsky.core_db.internal.delete
import com.varpihovsky.core_db.internal.keyById
import com.varpihovsky.core_db.internal.putSingle
import com.varpihovsky.repo_data.Confidential
import kotlinx.coroutines.flow.Flow
import org.kodein.db.DB
import org.kodein.db.flowOf

interface ConfidentialDAO : SingleEntryDAO<Confidential> {
    override fun get(): Flow<Confidential?>

    override fun set(t: Confidential)

    override fun delete()

    companion object {
        operator fun invoke(db: DB): ConfidentialDAO = ConfidentialDAOImpl(db)
    }
}

private class ConfidentialDAOImpl(private val db: DB) :
    ConfidentialDAO {
    override fun get(): Flow<Confidential?> {
        return db.flowOf(db.keyById())
    }

    override fun set(t: Confidential) {
        db.putSingle(t)
    }

    override fun delete() {
        db.delete<Confidential>()
    }

}