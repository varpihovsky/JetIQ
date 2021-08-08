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

import com.varpihovsky.core_db.dao.ContactDAO
import com.varpihovsky.core_network.managers.JetIQListManager
import com.varpihovsky.core_network.result.Result
import com.varpihovsky.core_repo.testCore.RepoTest
import com.varpihovsky.repo_data.ContactDTO
import com.varpihovsky.repo_data.ListItemDTO
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.last
import kotlin.test.Test
import kotlin.test.assertEquals

class ListRepoTest : RepoTest() {
    private lateinit var listRepo: ListRepo

    private val jetIQListManager: JetIQListManager = mockk(relaxed = true)
    private val contactDAO: ContactDAO = mockk(relaxed = true)

    @ExperimentalCoroutinesApi
    override fun setup() {
        super.setup()

        listRepo = ListRepo(jetIQListManager, contactDAO, exceptionEventManager)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun test_getFaculties_returns_faculties() = runBlockingTest {
        coEvery { jetIQListManager.getFaculties() } returns Result.success(TEST_FACULTIES)
        assertEquals(TEST_FACULTIES, listRepo.getFaculties())
    }

    @ExperimentalCoroutinesApi
    @Test
    fun test_getGroupByFaculty_returns_groups_by_faculty_id() = runBlockingTest {
        coEvery { jetIQListManager.getGroupsByFaculty(TEST_ID) } returns Result.success(TEST_GROUPS)
        assertEquals(TEST_GROUPS, listRepo.getGroupByFaculty(TEST_ID))
    }

    @ExperimentalCoroutinesApi
    @Test
    fun test_getStudentsByGroup_returns_students_by_group_id() = runBlockingTest {
        coEvery { jetIQListManager.getStudentsByGroup(TEST_ID) } returns Result.success(
            TEST_STUDENTS
        )
        assertEquals(TEST_STUDENTS, listRepo.getStudentsByGroup(TEST_ID))
    }

    @ExperimentalCoroutinesApi
    @Test
    fun test_getTeachersByQuery_returns_teachers_by_query() = runBlockingTest {
        coEvery { jetIQListManager.getTeacherByQuery(TEST_QUERY) } returns Result.success(
            TEST_TEACHERS
        )
        assertEquals(TEST_TEACHERS, listRepo.getTeacherByQuery(TEST_QUERY))
    }

    @ExperimentalCoroutinesApi
    @Test
    fun test_getContacts_returns_contacts_flow() = runBlockingTest {
        every { contactDAO.getContacts() } returns flow {
            emit(TEST_CONTACTS)
        }
        assertEquals(TEST_CONTACTS, listRepo.getContacts().last())
    }

    @Test
    fun test_addContact_insert_contact_inside_database() {
        listRepo.addContact(TEST_CONTACTS.first())
        verify { contactDAO.insert(TEST_CONTACTS.first()) }
    }

    @Test
    fun test_removeContact_deletes_contact_from_database() {
        listRepo.removeContact(TEST_CONTACTS.first())
        verify { contactDAO.delete(TEST_CONTACTS.first()) }
    }

    @Test
    fun test_clear_clears_database() {
        listRepo.clear()
        verify { contactDAO.clear() }
    }

    companion object {
        private const val TEST_ID = 0
        private const val TEST_QUERY = "Example Teacher"

        private val TEST_FACULTIES = listOf(ListItemDTO(TEST_ID, "Example faculty"))
        private val TEST_GROUPS = listOf(ListItemDTO(TEST_ID, "Example group"))
        private val TEST_STUDENTS = listOf(ListItemDTO(TEST_ID, "Example student"))
        private val TEST_TEACHERS = listOf(ListItemDTO(TEST_ID, TEST_QUERY))

        private val TEST_CONTACTS = listOf(ContactDTO(TEST_ID, TEST_QUERY, ContactDTO.TYPE_TEACHER))
    }
}