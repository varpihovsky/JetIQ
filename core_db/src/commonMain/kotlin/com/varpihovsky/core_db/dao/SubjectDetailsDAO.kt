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

import com.varpihovsky.core_db.internal.DataFetcher
import com.varpihovsky.core_db.internal.allList
import com.varpihovsky.core_db.internal.deleteAll
import com.varpihovsky.core_db.internal.putListable
import com.varpihovsky.core_db.internal.types.MarkbookSubjectInternal
import com.varpihovsky.core_db.internal.types.SubjectDetailsInternal
import com.varpihovsky.core_db.internal.types.mappers.toExternal
import com.varpihovsky.core_db.internal.types.mappers.toInternal
import com.varpihovsky.jetiqApi.data.MarkbookSubject
import com.varpihovsky.jetiqApi.data.SubjectDetails
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import org.kodein.db.DB
import org.kodein.db.flowOf
import org.kodein.db.keyById
import org.kodein.db.on

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

private class SubjectDetailsDAOImpl(private val db: DB) :
    SubjectDetailsDAO {
    private val detailsFetcher: DataFetcher<SubjectDetailsInternal>
    private val markbookFetcher: DataFetcher<MarkbookSubjectInternal>

    init {
        // allList throws exception when it is joined with declaration.
        detailsFetcher = DataFetcher(db.allList())
        markbookFetcher = DataFetcher(db.allList())
        db.on<SubjectDetailsInternal>().register(detailsFetcher)
        db.on<MarkbookSubjectInternal>().register(markbookFetcher)
    }

    override fun insertDetails(subjectDetailsDTO: SubjectDetails) {
        val details = subjectDetailsDTO.toInternal()
        db.putListable(model = details)
    }

    override fun insertMarkbookSubject(markbookSubjectDTO: MarkbookSubject) {
        db.putListable(model = markbookSubjectDTO.toInternal())
    }

    override fun deleteAllDetails() {
        db.deleteAll<SubjectDetailsInternal>()
    }

    override fun deleteAllMarkbookSubjects() {
        db.deleteAll<MarkbookSubjectInternal>()
    }

    override fun getDetails(): Flow<List<SubjectDetails>> {
        return detailsFetcher.flow.map { it.map { internal -> internal.toExternal() } }
    }

    override fun getDetailsById(id: Int): Flow<SubjectDetails?> {
        return db.flowOf<SubjectDetailsInternal>(db.keyById(id)).filter { it?.id == id }
            .map { it?.toExternal() }
    }

    override fun getMarkbookSubjects(): Flow<List<MarkbookSubject>> {
        return markbookFetcher.flow
            .map { it.map { internal -> internal.toExternal() } }
    }

    override fun getMarkbookSubjectsList(): List<MarkbookSubject> {
        return db.allList<MarkbookSubjectInternal>().map { it.toExternal() }
    }

    override fun getMarkbookSubjectById(id: Int): Flow<MarkbookSubject?> {
        return db.flowOf<MarkbookSubjectInternal>(db.keyById(id)).filter { it?.id == id }
            .map { it?.toExternal() }
    }

}