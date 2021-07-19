package com.varpihovsky.core_repo.repo

import com.varpihovsky.core.exceptions.ExceptionEventManager
import com.varpihovsky.core_db.dao.ContactDAO
import com.varpihovsky.core_network.managers.JetIQListManager
import com.varpihovsky.repo_data.ContactDTO
import com.varpihovsky.repo_data.ListItemDTO
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface ListRepo {
    suspend fun getFaculties(): List<ListItemDTO>
    suspend fun getGroupByFaculty(facultyID: Int): List<ListItemDTO>
    suspend fun getStudentsByGroup(groupId: Int): List<ListItemDTO>

    suspend fun getTeacherByQuery(query: String): List<ListItemDTO>

    fun getContacts(): Flow<List<ContactDTO>>
    fun addContact(contactDTO: ContactDTO)
    fun removeContact(contactDTO: ContactDTO)

    fun clear()

    companion object {
        operator fun invoke(
            jetIQListManager: JetIQListManager,
            contactDAO: ContactDAO,
            exceptionEventManager: ExceptionEventManager
        ): ListRepo = ListRepoImpl(
            jetIQListManager,
            contactDAO,
            exceptionEventManager
        )
    }
}

private class ListRepoImpl @Inject constructor(
    private val jetIQListManager: JetIQListManager,
    private val contactDAO: ContactDAO,
    exceptionEventManager: ExceptionEventManager
) : ListRepo, Repo(exceptionEventManager) {
    override suspend fun getFaculties(): List<ListItemDTO> {
        return wrapException(
            result = jetIQListManager.getFaculties(),
            onSuccess = { it.value },
            onFailure = { listOf() }
        )
    }

    override suspend fun getGroupByFaculty(facultyID: Int): List<ListItemDTO> {
        return wrapException(
            result = jetIQListManager.getGroupsByFaculty(facultyID),
            onSuccess = { it.value },
            onFailure = { listOf() }
        )
    }

    override suspend fun getStudentsByGroup(groupId: Int): List<ListItemDTO> {
        return wrapException(
            result = jetIQListManager.getStudentsByGroup(groupId),
            onSuccess = { it.value },
            onFailure = { listOf() }
        )
    }

    override suspend fun getTeacherByQuery(query: String): List<ListItemDTO> {
        return wrapException(
            result = jetIQListManager.getTeacherByQuery(query),
            onSuccess = { it.value },
            onFailure = { listOf() }
        )
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