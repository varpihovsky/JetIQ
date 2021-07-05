package com.varpihovsky.jetiq.back.api.managers

import com.varpihovsky.jetiq.back.api.JetIQApi
import com.varpihovsky.jetiq.back.api.JetIQManager
import com.varpihovsky.jetiq.back.dto.ListItemDTO
import com.varpihovsky.jetiq.back.dto.util.ListItemJsonKey
import com.varpihovsky.jetiq.back.dto.util.deserializeListItem
import com.varpihovsky.jetiq.back.dto.util.deserializeTeachers
import com.varpihovsky.jetiq.system.ConnectionManager
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