package com.varpihovsky.core_network.managers

import com.varpihovsky.core_network.result.Result
import com.varpihovsky.core_network.result.asSuccess
import com.varpihovsky.core_network.testCore.JetIQNetworkManagerTest
import com.varpihovsky.repo_data.ListItemDTO
import io.mockk.coEvery
import io.mockk.every
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test

class JetIQListManagerTest : JetIQNetworkManagerTest() {
    private lateinit var jetIQListManager: JetIQListManager

    @ExperimentalCoroutinesApi
    override fun setup() {
        super.setup()
        jetIQListManager = JetIQListManager(jetIQApi)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `Test redirects exception`() = runBlockingTest {
        coEvery { jetIQApi.getFaculties() } returns RESULT_FAILURE
        assertEquals(RESULT_FAILURE, jetIQListManager.getFaculties())
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `Test getFaculties parses json rightfully`() = runBlockingTest {
        coEvery { jetIQApi.getFaculties() } returns Result.Success.Value(responseBody)
        every { responseBody.string() } returns TEST_FACULTIES_JSON
        assertEquals(TEST_FACULTIES_LIST, jetIQListManager.getFaculties().asSuccess().value)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `Test getGroupsByFaculty parses json rightfully`() = runBlockingTest {
        coEvery { jetIQApi.getGroupByFaculty(facultyId = any()) } returns Result.Success.Value(
            responseBody
        )
        every { responseBody.string() } returns TEST_GROUPS_JSON
        assertEquals(TEST_GROUPS_LIST, jetIQListManager.getGroupsByFaculty(0).asSuccess().value)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `Test getStudentsByGroup parses json rightfully`() = runBlockingTest {
        coEvery { jetIQApi.getStudentByGroup(groupId = any()) } returns Result.Success.Value(
            responseBody
        )
        every { responseBody.string() } returns TEST_STUDENTS_JSON
        assertEquals(TEST_STUDENTS_LIST, jetIQListManager.getStudentsByGroup(0).asSuccess().value)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `Test getTeacherByQuery parses json rightfully`() = runBlockingTest {
        coEvery { jetIQApi.getTeachersByQuery(query = any()) } returns Result.Success.Value(
            responseBody
        )
        every { responseBody.string() } returns TEST_TEACHERS_JSON
        assertEquals(TEST_TEACHERS_LIST, jetIQListManager.getTeacherByQuery("").asSuccess().value)
    }

    companion object {
        const val TEST_FACULTIES_JSON = "{\n" +
                "  \"d_name\": {\n" +
                "    \"267\": \"Інститут докторантури та аспірантури\",\n" +
                "    \"207\": \"Інститут екологічної безпеки та моніторингу довкілля\"\n" +
                "  }\n" +
                "}"

        val TEST_FACULTIES_LIST = listOf(
            ListItemDTO(267, "Інститут докторантури та аспірантури"),
            ListItemDTO(207, "Інститут екологічної безпеки та моніторингу довкілля")
        )

        const val TEST_GROUPS_JSON = "{\n" +
                "  \"st_group\": {\n" +
                "    \"5576\": \"015-17а\",\n" +
                "    \"5668\": \"015-18а\"\n" +
                "  },\n" +
                "  \"spec_id\": {\n" +
                "    \"5576\": \"4705\",\n" +
                "    \"5668\": \"4705\"\n" +
                "  }\n" +
                "}"

        val TEST_GROUPS_LIST = listOf(
            ListItemDTO(5576, "015-17а"),
            ListItemDTO(5668, "015-18а")
        )

        const val TEST_STUDENTS_JSON = "{\n" +
                "  \"s_name\": {\n" +
                "    \"3648\": \"Мельничук Лариса Василівна\"\n" +
                "  }\n" +
                "}"

        val TEST_STUDENTS_LIST = listOf(
            ListItemDTO(3648, "Мельничук Лариса Василівна")
        )

        const val TEST_TEACHERS_JSON = "{\n" +
                "  \"t_name\": {\n" +
                "    \"2\": \"Паламарчук Євген Анатолійович\",\n" +
                "    \"1871\": \"Паламарчук Степан Степанович\"\n" +
                "  }\n" +
                "}"

        val TEST_TEACHERS_LIST = listOf(
            ListItemDTO(2, "Паламарчук Євген Анатолійович"),
            ListItemDTO(1871, "Паламарчук Степан Степанович")
        )
    }
}