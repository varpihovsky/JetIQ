package com.varpihovsky.core_network.managers

import com.varpihovsky.core_network.JetIQApi
import com.varpihovsky.core_network.JetIQManager
import com.varpihovsky.core_network.parsers.ListItemJsonKey
import com.varpihovsky.core_network.parsers.deserializeListItem
import com.varpihovsky.core_network.parsers.deserializeTeachers
import com.varpihovsky.core_network.result.Result
import com.varpihovsky.repo_data.ListItemDTO

interface JetIQListManager {
    suspend fun getFaculties(): Result<List<ListItemDTO>>

    suspend fun getGroupsByFaculty(facultyId: Int): Result<List<ListItemDTO>>

    suspend fun getStudentsByGroup(groupId: Int): Result<List<ListItemDTO>>

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