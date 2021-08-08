package com.varpihovsky.core_network.managers

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

import com.varpihovsky.core_network.JetIQApi
import com.varpihovsky.core_network.JetIQManager
import com.varpihovsky.core_network.parsers.ListItemJsonKey
import com.varpihovsky.core_network.parsers.deserializeListItem
import com.varpihovsky.core_network.parsers.deserializeTeachers
import com.varpihovsky.core_network.result.Result
import com.varpihovsky.repo_data.ListItemDTO

/**
 * Interface with which you can get public VNTU data like faculties, student groups, students or teachers.
 *
 * @author Vladyslav Podrezenko
 */
interface JetIQListManager {
    /**
     * Returns list of faculties.
     *
     * @return list of [ListItemDTO]
     */
    suspend fun getFaculties(): Result<List<ListItemDTO>>

    /**
     * Returns list of student groups by faculty.
     *
     * @param facultyId id of faculty got in [getFaculties]
     *
     * @return list of [ListItemDTO]
     */
    suspend fun getGroupsByFaculty(facultyId: Int): Result<List<ListItemDTO>>

    /**
     * Returns list of students by group.
     *
     * @param groupId id of group got in [getGroupsByFaculty]
     *
     * @return list of [ListItemDTO]
     */
    suspend fun getStudentsByGroup(groupId: Int): Result<List<ListItemDTO>>

    /**
     * Returns list of teachers by query. If there are no teacher with name containing specified query
     * returns empty list.
     *
     * @param query should contain teacher name.
     *
     * @return list of [ListItemDTO]
     */
    suspend fun getTeacherByQuery(query: String): Result<List<ListItemDTO>>

    companion object {
        operator fun invoke(jetIQApi: JetIQApi): JetIQListManager =
            JetIQListManagerImpl(jetIQApi)
    }
}


private class JetIQListManagerImpl(private val jetIQApi: JetIQApi) : JetIQManager(),
    JetIQListManager {
    override suspend fun getFaculties(): Result<List<ListItemDTO>> {
        return mapResult(jetIQApi.getFaculties()) {
            Result.Success.Value(deserializeListItem(it.value.string(), ListItemJsonKey.FACULTY))
        }
    }

    override suspend fun getGroupsByFaculty(facultyId: Int): Result<List<ListItemDTO>> {
        return mapResult(jetIQApi.getGroupByFaculty(facultyId = facultyId)) {
            Result.Success.Value(deserializeListItem(it.value.string(), ListItemJsonKey.GROUP))
        }
    }

    override suspend fun getStudentsByGroup(groupId: Int): Result<List<ListItemDTO>> {
        return mapResult(jetIQApi.getStudentByGroup(groupId = groupId)) {
            Result.Success.Value(deserializeListItem(it.value.string(), ListItemJsonKey.STUDENT))
        }
    }

    override suspend fun getTeacherByQuery(query: String): Result<List<ListItemDTO>> {
        return mapResult(jetIQApi.getTeachersByQuery(query = query)) {
            Result.Success.Value(deserializeTeachers(it.value.string()))
        }
    }
}