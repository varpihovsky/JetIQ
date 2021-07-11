package com.varpihovsky.jetiq.screens.profile

import com.varpihovsky.jetiq.back.dto.MarkbookSubjectDTO
import com.varpihovsky.jetiq.back.dto.ProfileDTO
import com.varpihovsky.jetiq.back.dto.SubjectDTO
import com.varpihovsky.jetiq.back.dto.SubjectDetailsDTO
import com.varpihovsky.jetiq.back.model.ProfileModel
import com.varpihovsky.jetiq.back.model.SubjectModel
import com.varpihovsky.jetiq.system.ConnectionManager
import com.varpihovsky.jetiq.testCore.ViewModelTest
import com.varpihovsky.jetiq.ui.dto.MarksInfo
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

    private val profileModel: ProfileModel = mockk(relaxed = true)
    private val subjectModel: SubjectModel = mockk(relaxed = true)

    @ExperimentalCoroutinesApi
    @Test
    fun `Test subject flow processing is working`() = runBlockingTest {
        every { subjectModel.getSubjectList() } returns flow {
            emit(listOf(TEST_SUBJECTS.first()))
        }
        every { subjectModel.getSubjectDetailsList() } returns flow {
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
        every { subjectModel.getMarkbookSubjects() } returns flow {
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
        profileInteractor.refresh()
        verify { subjectModel.removeAllSubjects() }
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
            ProfileInteractor(viewModelDispatchers, profileModel, subjectModel, ConnectionManager())
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