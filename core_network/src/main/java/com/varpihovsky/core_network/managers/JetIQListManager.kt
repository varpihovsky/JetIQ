package com.varpihovsky.core_network.managers

import com.varpihovsky.core.ConnectionManager
import com.varpihovsky.core_network.JetIQApi
import com.varpihovsky.core_network.JetIQManager
import com.varpihovsky.core_network.parsers.ListItemJsonKey
import com.varpihovsky.core_network.parsers.deserializeListItem
import com.varpihovsky.core_network.parsers.deserializeTeachers
import com.varpihovsky.repo_data.ListItemDTO
import javax.inject.Inject

class JetIQListManager @Inject constructor(
    private val jetIQApi: JetIQApi,
    connectionManager: ConnectionManager
) : JetIQManager(connectionManager) {
    fun getFaculties(): List<ListItemDTO> {
        return deserializeListItem(
            exceptionWrap { jetIQApi.getFaculties().execute() }!!.string(),
            ListItemJsonKey.FACULTY
        )
    }

    fun getGroupsByFaculty(facultyId: Int): List<ListItemDTO> {
        return deserializeListItem(
            exceptionWrap {
                jetIQApi.getGroupByFaculty(facultyId = facultyId).execute()
            }!!.string(),
            ListItemJsonKey.GROUP
        )
    }

    fun getStudentsByGroup(groupId: Int): List<ListItemDTO> {
        return deserializeListItem(
            exceptionWrap { jetIQApi.getStudentByGroup(groupId = groupId).execute() }!!.string(),
            ListItemJsonKey.STUDENT
        )
    }

    fun getTeacherByQuery(query: String): List<ListItemDTO> {
        return deserializeTeachers(exceptionWrap {
            jetIQApi.getTeachersByQuery(query = query).execute()
        }!!.string())

    }
}