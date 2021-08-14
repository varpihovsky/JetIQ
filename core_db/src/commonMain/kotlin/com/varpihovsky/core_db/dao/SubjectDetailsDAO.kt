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
import com.varpihovsky.core_db.internal.get
import com.varpihovsky.core_db.internal.types.MarkbookSubjectInternal
import com.varpihovsky.core_db.internal.types.SubjectDetailsInternal
import com.varpihovsky.core_db.internal.types.TaskInternal
import com.varpihovsky.core_db.internal.types.lists.MarkbookSubjectList
import com.varpihovsky.core_db.internal.types.lists.SubjectDetailsList
import com.varpihovsky.core_db.internal.types.mappers.toExternal
import com.varpihovsky.core_db.internal.types.mappers.toInternal
import com.varpihovsky.jetiqApi.data.MarkbookSubject
import com.varpihovsky.jetiqApi.data.SubjectDetails
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.kodein.db.*

interface SubjectDetailsDAO {
    fun insertDetails(subjectDetailsDTO: SubjectDetails)

    fun insertMarkbookSubject(markbookSubjectDTO: MarkbookSubject)

    fun deleteAllDetails()

    fun deleteAllMarkbookSubjects()

    fun getDetails(): Flow<List<SubjectDetails>>

    fun getDetailsById(id: Int): Flow<SubjectDetails?>

    fun getMarkbookSubjects(): Flow<List<MarkbookSubject>>

    fun getMarkbookSubjectsList(): List<MarkbookSubject>

    fun getMarkbookSubjectById(id: Int): Flow<MarkbookSubject?>

    companion object {
        operator fun invoke(db: DB): SubjectDetailsDAO = SubjectDetailsDAOImpl(db)
    }
}

private class SubjectDetailsDAOImpl(private val db: DB) : SubjectDetailsDAO {
    override fun insertDetails(subjectDetailsDTO: SubjectDetails) {
        val tasks = subjectDetailsDTO.tasks.map { it.toInternal() }.map { db.put(it) }
        val details = subjectDetailsDTO.toInternal(tasks)
        db.put(PutPolicy.AS_IS, details) { SubjectDetailsList(listOf()) }
    }

    override fun insertMarkbookSubject(markbookSubjectDTO: MarkbookSubject) {
        db.put(PutPolicy.LAST, markbookSubjectDTO.toInternal()) { MarkbookSubjectList(listOf()) }
    }

    override fun deleteAllDetails() {
        val cursor = db.find<TaskInternal>().all()
        db.deleteAll(cursor)

        cursor.close()

        db.deleteAll<SubjectDetailsList, SubjectDetailsInternal>()
    }

    override fun deleteAllMarkbookSubjects() {
        db.deleteAll<MarkbookSubjectList, MarkbookSubjectInternal>()
    }

    override fun getDetails(): Flow<List<SubjectDetails>> {
        val details = db.listFlow<SubjectDetailsList, SubjectDetailsInternal>()
        return details.map { list ->
            list.map { details ->
                val tasks = details.tasks.mapNotNull { key -> db.get(key)?.toExternal() }
                details.toExternal(tasks)
            }
        }
    }

    override fun getDetailsById(id: Int): Flow<SubjectDetails?> {
        val detailsFlow = db.flowOf<SubjectDetailsInternal>(db.keyById(id))
        return detailsFlow.map { details ->
            val tasks = details?.tasks?.mapNotNull { key -> db.get(key)?.toExternal() }
            tasks?.let { details.toExternal(it) }
        }
    }

    override fun getMarkbookSubjects(): Flow<List<MarkbookSubject>> {
        return db.listFlow<MarkbookSubjectList, MarkbookSubjectInternal>()
            .map { it.map { internal -> internal.toExternal() } }
    }

    override fun getMarkbookSubjectsList(): List<MarkbookSubject> {
        return db.allList<MarkbookSubjectInternal>().map { it.toExternal() }
    }

    override fun getMarkbookSubjectById(id: Int): Flow<MarkbookSubject?> {
        return db.flowOf<MarkbookSubjectInternal>(db.keyById(id)).map { it?.toExternal() }
    }

}