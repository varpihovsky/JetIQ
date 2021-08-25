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
package com.varpihovsky.jetiqApi

import com.varpihovsky.jetiqApi.data.*
import com.varpihovsky.jetiqApi.result.EmptyResult
import com.varpihovsky.jetiqApi.result.Result

interface Api {
    suspend fun authorize(
        login: String,
        password: String
    ): Result<Profile>

    suspend fun logout(
        cookie: String,
    ): EmptyResult

    suspend fun getSuccessJournal(
        cookie: String,
    ): Result<List<Subject>>

    suspend fun getSubjectDetails(
        cookie: String,
        subjectId: Int
    ): Result<SubjectDetails>

    suspend fun getMarkbookSubjects(
        cookie: String
    ): Result<List<MarkbookSubject>>

    suspend fun getMessages(
        cookie: String,
    ): Result<List<Message>>

    suspend fun getFaculties(): Result<List<Faculty>>

    suspend fun getGroupByFaculty(facultyId: Int): Result<List<Group>>

    suspend fun getStudentByGroup(groupId: Int): Result<List<Student>>

    suspend fun getTeachersByQuery(query: String): Result<List<Teacher>>

    suspend fun sendMessage(
        cookie: String,
        receiverId: Int,
        isTeacher: Int,
        message: String,
        csrf: Csrf
    ): EmptyResult


    suspend fun getCsrf(
        cookie: String
    ): Result<Csrf>

    companion object {
        internal const val url: String = "https://iq.vntu.edu.ua/b04213/curriculum/api.php"
    }
}