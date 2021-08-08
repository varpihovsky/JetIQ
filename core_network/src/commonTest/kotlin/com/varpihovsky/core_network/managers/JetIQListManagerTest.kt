package com.varpihovsky.core_network.managers

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

import com.varpihovsky.core_network.result.Result
import com.varpihovsky.core_network.result.asSuccess
import com.varpihovsky.core_network.testCore.JetIQNetworkManagerTest
import com.varpihovsky.repo_data.ListItemDTO
import io.mockk.coEvery
import io.mockk.every
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlin.test.Test
import kotlin.test.assertEquals

class JetIQListManagerTest : JetIQNetworkManagerTest() {
    private lateinit var jetIQListManager: JetIQListManager

    @ExperimentalCoroutinesApi
    override fun setup() {
        super.setup()
        jetIQListManager = JetIQListManager(jetIQApi)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun test_redirects_exception() = runBlockingTest {
        coEvery { jetIQApi.getFaculties() } returns RESULT_FAILURE
        assertEquals(RESULT_FAILURE, jetIQListManager.getFaculties())
    }

    @ExperimentalCoroutinesApi
    @Test
    fun test_getFaculties_parses_json_rightfully() = runBlockingTest {
        coEvery { jetIQApi.getFaculties() } returns Result.Success.Value(responseBody)
        every { responseBody.string() } returns TEST_FACULTIES_JSON
        assertEquals(TEST_FACULTIES_LIST, jetIQListManager.getFaculties().asSuccess().value)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun test_getGroupsByFaculty_parses_json_rightfully() = runBlockingTest {
        coEvery { jetIQApi.getGroupByFaculty(facultyId = any()) } returns Result.Success.Value(
            responseBody
        )
        every { responseBody.string() } returns TEST_GROUPS_JSON
        assertEquals(TEST_GROUPS_LIST, jetIQListManager.getGroupsByFaculty(0).asSuccess().value)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun test_getStudentsByGroup_parses_json_rightfully() = runBlockingTest {
        coEvery { jetIQApi.getStudentByGroup(groupId = any()) } returns Result.Success.Value(
            responseBody
        )
        every { responseBody.string() } returns TEST_STUDENTS_JSON
        assertEquals(TEST_STUDENTS_LIST, jetIQListManager.getStudentsByGroup(0).asSuccess().value)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun test_getTeacherByQuery_parses_json_rightfully() = runBlockingTest {
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