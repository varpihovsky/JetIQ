package com.varpihovsky.jetiq.back.model

import com.varpihovsky.jetiq.back.api.managers.JetIQListManager
import com.varpihovsky.jetiq.back.db.managers.ContactDatabaseManager
import com.varpihovsky.jetiq.back.dto.ContactDTO
import com.varpihovsky.jetiq.back.dto.ListItemDTO
import javax.inject.Inject

class ListModel @Inject constructor(
    private val jetIQListManager: JetIQListManager,
    private val contactDatabaseManager: ContactDatabaseManager
) {
    fun getFaculties(): List<ListItemDTO> {
        return jetIQListManager.getFaculties()
    }

    fun getGroupByFaculty(facultyID: Int): List<ListItemDTO> {
        return jetIQListManager.getGroupsByFaculty(facultyID)
    }

    fun getStudentsByGroup(groupId: Int): List<ListItemDTO> {
        return jetIQListManager.getStudentsByGroup(groupId)
    }

    fun getTeacherByQuery(query: String): List<ListItemDTO> {
        return jetIQListManager.getTeacherByQuery(query)
    }

    fun getContacts() = contactDatabaseManager.getContacts()

    fun addContact(contactDTO: ContactDTO) {
        contactDatabaseManager.addContact(contactDTO)
    }

    fun removeContact(contactDTO: ContactDTO) {
        contactDatabaseManager.removeContact(contactDTO)
    }

    fun clear() {
        contactDatabaseManager.clear()
    }
}