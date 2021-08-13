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

import com.varpihovsky.jetiqApi.Api.Companion.url
import com.varpihovsky.jetiqApi.config.ApiConfig
import com.varpihovsky.jetiqApi.config.ApiConfigBuilder
import com.varpihovsky.jetiqApi.data.*
import com.varpihovsky.jetiqApi.get
import com.varpihovsky.jetiqApi.internal.*
import com.varpihovsky.jetiqApi.result.*
import io.ktor.client.*
import io.ktor.client.request.*

fun provideApi(config: ApiConfigBuilder.() -> Unit): Api =
    ApiImpl(ApiConfigBuilder().apply(config).build())

private class ApiImpl(config: ApiConfig) : Api {
    private val client: HttpClient = config.httpClient

    override suspend fun authorize(login: String, password: String): Result<Profile> {
        val result: Result<Profile> = client.get(url) {
            parameter("login", login)
            parameter("pwd", password)
        }

        return if (result.isSuccess()) {
            val res = result.asHttpResponse()
            res.copy(value = res.value.withSession(res.headers["Cookie"]))
        } else result
    }

    override suspend fun logout(cookie: String): EmptyResult = client.get(url) {
        parameter("logout", 1)
        headers["Cookie"] = cookie
    }

    override suspend fun getSuccessJournal(cookie: String): Result<List<Subject>> =
        client.get<Result<RawJSON>>(url) {
            headers["Cookie"] = cookie
            parameter("action", "journ_list")
        }.map { deserializeSubjects(it) }

    override suspend fun getSubjectDetails(cookie: String, subjectId: Int): Result<SubjectDetails> =
        client.get<Result<RawJSON>>(url) {
            headers["Cookie"] = cookie
            parameter("card_id", subjectId)
            parameter("action", "journ_view")
        }.map { deserializeSubjectDetails(it) }

    override suspend fun getMarkbookSubjects(cookie: String): Result<List<MarkbookSubject>> =
        client.get<Result<RawJSON>>(url) {
            headers["Cookie"] = cookie
            parameter("markbook", 1)
        }.map { deserializeMarkbookSubjects(it) }


    override suspend fun getMessages(cookie: String): Result<List<Message>> = client.get(url) {
        headers["Cookie"] = cookie
        parameter("get_mess", 1)
    }

    override suspend fun getFaculties(): Result<List<Faculty>> = client.get<Result<RawJSON>>(url) {
        parameter("msg", 1)
        parameter("facult_list", 1)
    }.map { deserializeFaculties(it) }

    override suspend fun getGroupByFaculty(facultyId: Int): Result<List<Group>> =
        client.get<Result<RawJSON>>(url) {
            parameter("msg", 1)
            parameter("group_list", 1)
            parameter("f_id", facultyId)
        }.map { deserializeGroups(it) }

    override suspend fun getStudentByGroup(groupId: Int): Result<List<Student>> =
        client.get<Result<RawJSON>>(url) {
            parameter("msg", 1)
            parameter("stud_list", 1)
            parameter("group_id", groupId)
        }.map { deserializeStudents(it) }

    override suspend fun getTeachersByQuery(query: String): Result<List<Teacher>> =
        client.get<Result<RawJSON>>(url) {
            parameter("msg", 1)
            parameter("t_list", 1)
            parameter("t_name", query)
        }.map { deserializeTeachers(it) }

    override suspend fun sendMessage(
        cookie: String,
        receiverId: Int,
        isTeacher: Int,
        message: String,
        csrf: Csrf
    ): EmptyResult = client.get(url) {
        headers["Cookie"] = cookie
        parameter("msg", 1)
        parameter("send", 1)
        parameter("s_id", receiverId)
        parameter("isteacher", isTeacher)
        parameter("message", message)
        parameter("csrf", csrf.body)
    }

    override suspend fun getCsrf(cookie: String): Result<Csrf> = client.get(url) {
        headers["Cookie"] = cookie
        parameter("msg", 1)
        parameter("pre", 1)
    }

    private fun <T> Result<RawJSON>.map(valueFactory: (String) -> T): Result<T> =
        if (isSuccess()) {
            val processed = asHttpResponse()
            Result.Success.HttpResponse(
                valueFactory(processed.value.json),
                processed.statusCode,
                processed.url,
                processed.headers
            )
        } else {
            asFailure()
        }
}
