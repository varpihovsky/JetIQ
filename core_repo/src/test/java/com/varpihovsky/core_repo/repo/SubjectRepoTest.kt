package com.varpihovsky.core_repo.repo

import com.varpihovsky.core_db.dao.SubjectDAO
import com.varpihovsky.core_db.dao.SubjectDetailsDAO
import com.varpihovsky.core_network.managers.JetIQSubjectManager
import com.varpihovsky.core_network.result.Result
import com.varpihovsky.core_repo.testCore.ConfidentRepoTest
import com.varpihovsky.repo_data.MarkbookSubjectDTO
import com.varpihovsky.repo_data.SubjectDTO
import com.varpihovsky.repo_data.SubjectDetailsDTO
import com.varpihovsky.repo_data.SubjectTaskDTO
import com.varpihovsky.repo_data.relations.SubjectDetailsWithTasks
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test

class SubjectRepoTest : ConfidentRepoTest() {
    private lateinit var subjectRepo: SubjectRepo

    private val subjectDAO: SubjectDAO = mockk(relaxed = true)
    private val subjectDetailsDAO: SubjectDetailsDAO = mockk(relaxed = true)
    private val jetIQSubjectManager: JetIQSubjectManager = mockk(relaxed = true)

    @ExperimentalCoroutinesApi
    override fun setup() {
        super.setup()

        initMocks()
        initRepo()
    }

    private fun initRepo() {
        subjectRepo = SubjectRepo(
            subjectDAO,
            subjectDetailsDAO,
            jetIQSubjectManager,
            confidentialDAO,
            profileDAO
        )
    }

    private fun initMocks() {
        coEvery { jetIQSubjectManager.getSuccessJournal(TEST_PROFILE.session!!) } returns Result.success(
            TEST_SUBJECTS
        )
        coEvery {
            jetIQSubjectManager.getSubjectDetails(TEST_PROFILE.session!!, 1)
        } returns Result.success(TEST_SUBJECT_DETAILS)
        coEvery { jetIQSubjectManager.getMarkbookSubjects(TEST_PROFILE.session!!) } returns Result.success(
            TEST_MARKBOOK
        )
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `Test loadSuccessJournal adds subjects to database`() = runBlockingTest {
        subjectRepo.loadSuccessJournal()
        verify { subjectDAO.insert(TEST_SUBJECTS.first()) }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `Test loadSuccessJournal adds subjectDetails to database`() = runBlockingTest {
        subjectRepo.loadSuccessJournal()
        verifyAll {
            subjectDetailsDAO.insertDetails(TEST_SUBJECT_DETAILS.subjectDetailsDTO)
            subjectDetailsDAO.insertTask(TEST_SUBJECT_DETAILS.subjectTasks.first())
        }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `Test loadMarkbookSubjects adds markbook subject to database`() = runBlockingTest {
        every { subjectDAO.getAllSubjectsList() } returns TEST_SUBJECTS
        subjectRepo.loadMarkbookSubjects()
        verify { subjectDetailsDAO.insertMarkbookSubject(TEST_MARKBOOK.first()) }
    }

    companion object {
        val TEST_SUBJECTS = listOf(
            SubjectDTO(
                "1",
                "",
                "",
                "1",
                "Example",
                "Example"
            )
        )

        val TEST_SUBJECT_DETAILS = SubjectDetailsWithTasks(
            SubjectDetailsDTO(
                1,
                "",
                1,
                1,
                1,
                1,
                1,
                1,
                2,
                2,
                2,
                2
            ),
            listOf(
                SubjectTaskDTO(
                    1,
                    1,
                    "",
                    "",
                    1
                )
            )
        )

        val TEST_MARKBOOK = listOf(
            MarkbookSubjectDTO(
                1,
                "",
                "",
                "",
                "",
                "",
                "",
                "Example",
                "Example",
                1,
                1
            )
        )
    }
}