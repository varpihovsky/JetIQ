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

import com.varpihovsky.core_db.internal.*
import com.varpihovsky.core_db.internal.types.SubjectInternal
import com.varpihovsky.core_db.internal.types.mappers.toExternal
import com.varpihovsky.core_db.internal.types.mappers.toInternal
import com.varpihovsky.jetiqApi.data.Subject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import org.kodein.db.DB
import org.kodein.db.flowOf
import org.kodein.db.keyById
import org.kodein.db.on

interface SubjectDAO {
    fun getSubjectById(id: String): Flow<Subject?>

    fun getAllSubjects(): Flow<List<Subject>>

    fun getAllSubjectsList(): List<Subject>

    fun insert(subjectDTO: Subject)

    fun delete(subjectDTO: Subject)

    fun deleteSubjectById(id: Int)

    fun deleteAll()

    companion object {
        operator fun invoke(db: DB): SubjectDAO = SubjectDAOImpl(db)
    }
}

private class SubjectDAOImpl(private val db: DB) : SubjectDAO {
    private val dataFetcher = DataFetcher<SubjectInternal>(db.allList())

    init {
        db.on<SubjectInternal>().register(dataFetcher)
    }

    override fun getSubjectById(id: String): Flow<Subject?> {
        return db.flowOf<SubjectInternal>(db.keyById(id.toInt())).filter { it?.id == id.toInt() }
            .map { it?.toExternal() }
    }

    override fun getAllSubjects(): Flow<List<Subject>> {
        return dataFetcher.flow.map { it.map { internal -> internal.toExternal() } }
    }

    override fun getAllSubjectsList(): List<Subject> {
        return db.allList<SubjectInternal>().map { it.toExternal() }
    }

    override fun insert(subjectDTO: Subject) {
        db.putListable(model = subjectDTO.toInternal())
    }

    override fun delete(subjectDTO: Subject) {
        db.delete(subjectDTO.toInternal())
    }

    override fun deleteSubjectById(id: Int) {
        val key = db.keyById<SubjectInternal>(id)
        db.get(key)?.let { db.delete(it) }
    }

    override fun deleteAll() {
        db.deleteAll<SubjectInternal>()
    }
}