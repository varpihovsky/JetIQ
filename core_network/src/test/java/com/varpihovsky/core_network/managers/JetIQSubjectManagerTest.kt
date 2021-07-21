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
import com.varpihovsky.repo_data.MarkbookSubjectDTO
import com.varpihovsky.repo_data.SubjectDTO
import com.varpihovsky.repo_data.SubjectDetailsDTO
import com.varpihovsky.repo_data.SubjectTaskDTO
import com.varpihovsky.repo_data.relations.SubjectDetailsWithTasks
import io.mockk.coEvery
import io.mockk.every
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test

class JetIQSubjectManagerTest : JetIQNetworkManagerTest() {
    private lateinit var jetIQSubjectManager: JetIQSubjectManager

    @ExperimentalCoroutinesApi
    override fun setup() {
        super.setup()

        jetIQSubjectManager = JetIQSubjectManager(jetIQApi)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `Test getSuccessJournal parses json rightfully`() = runBlockingTest {
        coEvery { jetIQApi.getSuccessJournal(cookie = any()) } returns Result.Success.Value(
            responseBody
        )
        every { responseBody.string() } returns SUCCESS_JOURNAL_JSON
        assertEquals(
            SUCCESS_JOURNAL_LIST,
            jetIQSubjectManager.getSuccessJournal("").asSuccess().value
        )
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `Test getSubjectDetails parses json rightfully`() = runBlockingTest {
        coEvery { jetIQApi.getSubjectDetails(any(), any()) } returns Result.Success.Value(
            responseBody
        )
        every { responseBody.string() } returns SUBJECT_DETAILS_JSON
        assertEquals(
            SUBJECT_DETAILS_OBJECT,
            jetIQSubjectManager.getSubjectDetails("", 0).asSuccess().value
        )
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `Test getMarkbookSubjects parses json rightfully`() = runBlockingTest {
        coEvery { jetIQApi.getMarkbookSubjects(any()) } returns Result.Success.Value(responseBody)
        every { responseBody.string() } returns MARKBOOK_JSON
        assertEquals(MARKBOOK_LIST, jetIQSubjectManager.getMarkbookSubjects("").asSuccess().value)
    }

    companion object {
        const val SUCCESS_JOURNAL_JSON = "{\n" +
                "  \"1\": {\n" +
                "    \"card_id\": \"17793\",\n" +
                "    \"subject\": \"Комп`ютерна графіка\",\n" +
                "    \"sem\": \"1\",\n" +
                "    \"t_name\": \"Софина Ольга Юріївна\",\n" +
                "    \"scale\": \"0\",\n" +
                "    \"f_control\": \"Модулі\"\n" +
                "  },\n" +
                "  \"2\": {\n" +
                "    \"card_id\": \"25932\",\n" +
                "    \"subject\": \"Технології WEB-картографування\",\n" +
                "    \"sem\": \"2\",\n" +
                "    \"t_name\": \"Крижановський Євгеній Миколайович\",\n" +
                "    \"scale\": \"0\",\n" +
                "    \"f_control\": \"Курсова робота\"\n" +
                "  }\n" +
                "}"

        val SUCCESS_JOURNAL_LIST = listOf(
            SubjectDTO(
                "17793",
                "Модулі",
                "0",
                "1",
                "Комп`ютерна графіка",
                "Софина Ольга Юріївна"
            ),
            SubjectDTO(
                "25932",
                "Курсова робота",
                "0",
                "2",
                "Технології WEB-картографування",
                "Крижановський Євгеній Миколайович"
            )
        )

        const val SUBJECT_DETAILS_JSON = "{\n" +
                "  \"0\": {\n" +
                "    \"legend\": \"Тест. Редактор растрової графіки Adobe Photoshop. 5 балів\",\n" +
                "    \"num_mod\": \"1\",\n" +
                "    \"points\": 4\n" +
                "  },\n" +
                "  \"1\": {\n" +
                "    \"legend\": \"Додаткові бали. Презентації. 3 бали\",\n" +
                "    \"num_mod\": \"2\",\n" +
                "    \"points\": 0\n" +
                "  },\n" +
                "  \"sum1\": 32,\n" +
                "  \"sum2\": 27,\n" +
                "  \"mark1\": 4,\n" +
                "  \"mark2\": 3,\n" +
                "  \"h_pres1\": 0,\n" +
                "  \"h_pres2\": 0,\n" +
                "  \"for_pres1\": 0,\n" +
                "  \"for_pres2\": 0,\n" +
                "  \"total_prev\": 0,\n" +
                "  \"total\": 59,\n" +
                "  \"ects\": \"FX\"\n" +
                "}"

        val SUBJECT_DETAILS_OBJECT = SubjectDetailsWithTasks(
            SubjectDetailsDTO(
                0,
                "FX",
                0,
                0,
                0,
                0,
                4,
                3,
                32,
                27,
                59,
                0
            ),
            listOf(
                SubjectTaskDTO(
                    0,
                    0,
                    "Тест. Редактор растрової графіки Adobe Photoshop. 5 балів",
                    "1",
                    4
                ),
                SubjectTaskDTO(
                    0,
                    0,
                    "Додаткові бали. Презентації. 3 бали",
                    "2",
                    0
                )
            )
        )

        const val MARKBOOK_JSON = "{\n" +
                "  \"result\": \"success\",\n" +
                "  \"1\": {\n" +
                "    \"1\": {\n" +
                "      \"subj_name\": \"Вступ до фаху\",\n" +
                "      \"form\": \"Диференційований залік\",\n" +
                "      \"hours\": \"90\",\n" +
                "      \"credits\": \"3\",\n" +
                "      \"total\": 90,\n" +
                "      \"ects\": \"A\",\n" +
                "      \"mark\": \"5\",\n" +
                "      \"date\": \"2021-01-18\",\n" +
                "      \"teacher\": \"Богач Ілона Віталіївна\"\n" +
                "    }\n" +
                "  },\n" +
                "  \"2\": {\n" +
                "    \"1\": {\n" +
                "      \"subj_name\": \"Технології WEB-картографування\",\n" +
                "      \"form\": \"Курсова робота\",\n" +
                "      \"hours\": \"45\",\n" +
                "      \"credits\": \"1.5\",\n" +
                "      \"total\": 96,\n" +
                "      \"ects\": \"A\",\n" +
                "      \"mark\": \"5\",\n" +
                "      \"date\": \"2021-06-25\",\n" +
                "      \"teacher\": \"Крижановський Євгеній Миколайович\"\n" +
                "    }\n" +
                "  }\n" +
                "}"

        val MARKBOOK_LIST = listOf(
            MarkbookSubjectDTO(
                0,
                "3",
                "2021-01-18",
                "A",
                "Диференційований залік",
                "90",
                "5",
                "Вступ до фаху",
                "Богач Ілона Віталіївна",
                90,
                1
            ),
            MarkbookSubjectDTO(
                0,
                "1.5",
                "2021-06-25",
                "A",
                "Курсова робота",
                "45",
                "5",
                "Технології WEB-картографування",
                "Крижановський Євгеній Миколайович",
                96,
                2
            )
        )
    }
}