package com.varpihovsky.core_repo.repo

import com.varpihovsky.core_db.dao.ContactDAO
import com.varpihovsky.core_network.managers.JetIQListManager
import com.varpihovsky.repo_data.ContactDTO
import com.varpihovsky.repo_data.ListItemDTO
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface ListRepo {
    fun getFaculties(): List<ListItemDTO>
    fun getGroupByFaculty(facultyID: Int): List<ListItemDTO>
    fun getStudentsByGroup(groupId: Int): List<ListItemDTO>

    fun getTeacherByQuery(query: String): List<ListItemDTO>

    fun getContacts(): Flow<List<ContactDTO>>
    fun addContact(contactDTO: ContactDTO)
    fun removeContact(contactDTO: ContactDTO)

    fun clear()

    companion object {
        internal operator fun invoke(
            jetIQListManager: JetIQListManager,
            contactDAO: ContactDAO
        ): ListRepo = ListRepoImpl(jetIQListManager, contactDAO)
    }
}

private class ListRepoImpl @Inject constructor(
    private val jetIQListManager: JetIQListManager,
    private val contactDAO: ContactDAO
) : ListRepo {
    override fun getFaculties(): List<ListItemDTO> {
        return jetIQListManager.getFaculties()
    }

    override fun getGroupByFaculty(facultyID: Int): List<ListItemDTO> {
        return jetIQListManager.getGroupsByFaculty(facultyID)
    }

    override fun getStudentsByGroup(groupId: Int): List<ListItemDTO> {
        return jetIQListManager.getStudentsByGroup(groupId)
    }

    override fun getTeacherByQuery(query: String): List<ListItemDTO> {
        return jetIQListManager.getTeacherByQuery(query)
    }

    override fun getContacts() = contactDAO.getContacts()

    override fun addContact(contactDTO: ContactDTO) {
        contactDAO.insert(contactDTO)
    }

    override fun removeContact(contactDTO: ContactDTO) {
        contactDAO.delete(contactDTO)
    }

    override fun clear() {
        contactDAO.clear()
    }
}