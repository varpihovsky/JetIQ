package com.varpihovsky.core_repo.repo

import com.varpihovsky.core_db.dao.ContactDAO
import com.varpihovsky.core_network.managers.JetIQListManager
import com.varpihovsky.core_network.result.Result
import com.varpihovsky.core_repo.testCore.CoroutineTest
import com.varpihovsky.repo_data.ContactDTO
import com.varpihovsky.repo_data.ListItemDTO
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test

class ListRepoTest : CoroutineTest() {
    private lateinit var listRepo: ListRepo

    private val jetIQListManager: JetIQListManager = mockk(relaxed = true)
    private val contactDAO: ContactDAO = mockk(relaxed = true)

    @ExperimentalCoroutinesApi
    override fun setup() {
        super.setup()

        listRepo = ListRepo(jetIQListManager, contactDAO)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `Test getFaculties returns faculties`() = runBlockingTest {
        coEvery { jetIQListManager.getFaculties() } returns Result.success(TEST_FACULTIES)
        assertEquals(TEST_FACULTIES, listRepo.getFaculties())
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `Test getGroupByFaculty returns groups by faculty id`() = runBlockingTest {
        coEvery { jetIQListManager.getGroupsByFaculty(TEST_ID) } returns Result.success(TEST_GROUPS)
        assertEquals(TEST_GROUPS, listRepo.getGroupByFaculty(TEST_ID))
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `Test getStudentsByGroup returns students by group id`() = runBlockingTest {
        coEvery { jetIQListManager.getStudentsByGroup(TEST_ID) } returns Result.success(
            TEST_STUDENTS
        )
        assertEquals(TEST_STUDENTS, listRepo.getStudentsByGroup(TEST_ID))
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `Test getTeachersByQuery returns teachers by query`() = runBlockingTest {
        coEvery { jetIQListManager.getTeacherByQuery(TEST_QUERY) } returns Result.success(
            TEST_TEACHERS
        )
        assertEquals(TEST_TEACHERS, listRepo.getTeacherByQuery(TEST_QUERY))
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `Test getContacts returns contacts flow`() = runBlockingTest {
        every { contactDAO.getContacts() } returns flow {
            emit(TEST_CONTACTS)
        }
        assertEquals(TEST_CONTACTS, listRepo.getContacts().last())
    }

    @Test
    fun `Test addContact insert contact inside database`() {
        listRepo.addContact(TEST_CONTACTS.first())
        verify { contactDAO.insert(TEST_CONTACTS.first()) }
    }

    @Test
    fun `Test removeContact deletes contact from database`() {
        listRepo.removeContact(TEST_CONTACTS.first())
        verify { contactDAO.delete(TEST_CONTACTS.first()) }
    }

    @Test
    fun `Test clear clears database`() {
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