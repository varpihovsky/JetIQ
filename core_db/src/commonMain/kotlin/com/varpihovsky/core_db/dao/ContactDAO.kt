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

import com.varpihovsky.core_db.internal.DataFetcher
import com.varpihovsky.core_db.internal.allList
import com.varpihovsky.core_db.internal.deleteAll
import com.varpihovsky.core_db.internal.putListable
import com.varpihovsky.repo_data.ContactDTO
import kotlinx.coroutines.flow.Flow
import org.kodein.db.DB
import org.kodein.db.deleteById
import org.kodein.db.on

interface ContactDAO {
    fun getContacts(): Flow<List<ContactDTO>>

    fun insert(contactDTO: ContactDTO)

    fun delete(contactDTO: ContactDTO)

    fun clear()

    companion object {
        operator fun invoke(db: DB): ContactDAO = ContactDAOImpl(db)
    }
}

class ContactDAOImpl(private val db: DB) : ContactDAO {
    private val dataFetcher: DataFetcher<ContactDTO> = DataFetcher(db.allList())

    init {
        db.on<ContactDTO>().register(dataFetcher)
    }

    override fun getContacts(): Flow<List<ContactDTO>> {
        return dataFetcher.flow
    }

    override fun insert(contactDTO: ContactDTO) {
        db.putListable(model = contactDTO)
    }

    override fun delete(contactDTO: ContactDTO) {
        db.deleteById<ContactDTO>(contactDTO.id)
    }

    override fun clear() {
        db.deleteAll<ContactDTO>()
    }

}