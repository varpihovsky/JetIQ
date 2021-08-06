package com.varpihovsky.jetiq.screens.profile

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

import com.varpihovsky.core_repo.repo.ProfileRepo
import com.varpihovsky.core_repo.repo.SubjectRepo
import com.varpihovsky.jetiq.testCore.ViewModelTest
import com.varpihovsky.repo_data.MarkbookSubjectDTO
import com.varpihovsky.repo_data.ProfileDTO
import com.varpihovsky.repo_data.SubjectDTO
import com.varpihovsky.repo_data.SubjectDetailsDTO
import com.varpihovsky.ui_data.dto.MarksInfo
import com.varpihovsky.ui_data.mappers.toUIDTO
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test

class ProfileInteractorTest : ViewModelTest() {
    private lateinit var profileInteractor: ProfileInteractor

    private val profileModel: ProfileRepo = mockk(relaxed = true)
    private val subjectModel: SubjectRepo = mockk(relaxed = true)

    @ExperimentalCoroutinesApi
    @Test
    fun `Test subject flow processing is working`() = runBlockingTest {
        every { subjectModel.getSubjects() } returns flow {
            emit(listOf(TEST_SUBJECTS.first()))
        }
        every { subjectModel.getSubjectsDetails() } returns flow {
            emit(listOf(TEST_DETAILS.first()))
        }

        initInteractor()

        assertEquals(
            Pair(listOf(MarksInfo(1, 0)), listOf(TEST_SUBJECTS.first().toUIDTO(0))),
            profileInteractor.successData.last()
        )
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `Test markbook flow processing is working`() = runBlockingTest {
        every { subjectModel.getMarkbook() } returns flow {
            emit(listOf(TEST_MARKBOOK_SUBJECT))
        }
        initInteractor()
        assertEquals(
            Pair(listOf(MarksInfo(1, 100)), listOf(TEST_MARKBOOK_SUBJECT.toUIDTO())),
            profileInteractor.markbookData.last()
        )
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `Test refreshing destroys all the database`() = runBlockingTest {
        initInteractor()
        profileInteractor.onRefresh()
        verify { subjectModel.onRefresh() }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `Test profile flow processing is working`() = runBlockingTest {
        every { profileModel.getProfile() } returns flow {
            emit(TEST_PROFILE)
        }
        initInteractor()
        assertEquals(TEST_PROFILE.toUIDTO(), profileInteractor.profileData.first())
    }

    @ExperimentalCoroutinesApi
    private fun initInteractor() {
        profileInteractor =
            ProfileInteractor(viewModelDispatchers, profileModel, subjectModel)
    }

    companion object {
        val TEST_SUBJECTS = listOf(
            SubjectDTO("1", "", "", "1", "", ""),
            SubjectDTO("2", "", "", "1", "", "")
        )

        val TEST_DETAILS = listOf(
            SubjectDetailsDTO(
                1,
                "",
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0
            ),
            SubjectDetailsDTO(
                2,
                "",
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0
            )
        )

        val TEST_MARKBOOK_SUBJECT = MarkbookSubjectDTO(
            0,
            "",
            "",
            "",
            "",
            "",
            "0",
            "",
            "",
            100,
            1
        )


        val TEST_PROFILE = ProfileDTO(
            0,
            "",
            "Факультет комп'ютерних систем та автоматики",
            "",
            "",
            "",
            "",
            "",
            "0",
            "",
            "",
            "",
            "Name Surname Example"
        )
    }
}