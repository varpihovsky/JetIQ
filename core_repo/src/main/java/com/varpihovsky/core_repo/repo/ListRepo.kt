package com.varpihovsky.core_repo.repo

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

import com.varpihovsky.core.exceptions.ExceptionEventManager
import com.varpihovsky.core_db.dao.ContactDAO
import com.varpihovsky.core_network.managers.JetIQListManager
import com.varpihovsky.repo_data.ContactDTO
import com.varpihovsky.repo_data.ListItemDTO
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Interface used for controlling contacts-related data. To add contact you should map response from
 * methods [getStudentsByGroup] and [getTeacherByQuery] to [ContactDTO] class.
 *
 * @author Vladyslav Podrezenko
 */
interface ListRepo {
    /**
     * Returns list of received faculties if request is successful and empty list when response was
     * failed.
     *
     * @return list of [ListItemDTO]
     */
    suspend fun getFaculties(): List<ListItemDTO>

    /**
     * Returns list of groups by facultyId which you have got from [getFaculties] method. If request
     * was successful returns response value. If it was failed returns empty list.
     *
     * @return list of [ListItemDTO]
     */
    suspend fun getGroupByFaculty(facultyID: Int): List<ListItemDTO>

    /**
     * Returns list of students by group id which you have got from [getGroupByFaculty] method.
     * If request was successful returns response value. If it was failed returns empty list.
     *
     * @return list of [ListItemDTO]
     */
    suspend fun getStudentsByGroup(groupId: Int): List<ListItemDTO>

    /**
     * Returns list of teachers by query. If request was successful returns response value.
     * If it was failed returns empty list.
     *
     * @return list of [ListItemDTO]
     */
    suspend fun getTeacherByQuery(query: String): List<ListItemDTO>

    /**
     * Returns flow of contacts saved in database.
     *
     * @return list of [ContactDTO]
     */
    fun getContacts(): Flow<List<ContactDTO>>

    /**
     * Adds contact into database.
     */
    fun addContact(contactDTO: ContactDTO)

    /**
     * Removes contact from database with same id.
     */
    fun removeContact(contactDTO: ContactDTO)

    /**
     * Clears database.
     */
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