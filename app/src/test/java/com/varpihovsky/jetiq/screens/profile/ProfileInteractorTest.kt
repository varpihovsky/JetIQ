package com.varpihovsky.jetiq.screens.profile

import com.varpihovsky.jetiq.back.dto.ProfileDTO
import com.varpihovsky.jetiq.back.dto.SubjectDTO
import com.varpihovsky.jetiq.back.dto.SubjectDetailsDTO
import com.varpihovsky.jetiq.back.model.ProfileModel
import com.varpihovsky.jetiq.back.model.SubjectModel
import com.varpihovsky.jetiq.system.ConnectionManager
import io.mockk.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Test

class ProfileInteractorTest {
    private lateinit var profileInteractor: ProfileInteractor

    private val subscriber: ProfileInteractor.Interactable = mockk(relaxed = true)
    private val profileModel: ProfileModel = mockk(relaxed = true)
    private val subjectModel: SubjectModel = mockk(relaxed = true)

    private val exampleSubject1 = SubjectDTO("1", "", "", "1", "", "")
    private val exampleSubject2 = SubjectDTO("2", "", "", "1", "", "")
    private val exampleSubject3 = SubjectDTO("3", "", "", "1", "", "")
    private val exampleSubject4 = SubjectDTO("4", "", "", "1", "", "")
    private val exampleSubject5 = SubjectDTO("5", "", "", "1", "", "")

    private var exampleDetail1 = SubjectDetailsDTO(1, "", 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
    private var exampleDetail2 = SubjectDetailsDTO(2, "", 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
    private var exampleDetail3 = SubjectDetailsDTO(3, "", 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
    private var exampleDetail4 = SubjectDetailsDTO(4, "", 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
    private var exampleDetail5 = SubjectDetailsDTO(5, "", 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)

    @Test
    fun `Test subject flows processing is working`() = runBlocking {
        every { subjectModel.getSubjectList() } returns graduateFlow(
            0,
            exampleSubject1, exampleSubject2, exampleSubject3, exampleSubject4, exampleSubject5
        ).distinctUntilChanged()
        every { subjectModel.getSubjectDetailsList() } returns graduateFlow(
            0,
            exampleDetail1, exampleDetail2, exampleDetail3, exampleDetail4, exampleDetail5
        )

        profileInteractor = initInteractor()
        profileInteractor.subscribe(subscriber)

        delay(100)

        verify(atLeast = 4) { subscriber.onSuccessMarksInfoChange(any()) }
        verify(atLeast = 4) { subscriber.onSuccessSubjectsChange(any()) }
    }

    @Test
    fun `Test refreshing destroys all the database`() = runBlocking {
        profileInteractor = initInteractor()
        profileInteractor.refresh()
        verifyAll {
            subjectModel.removeAllSubjects()
            subjectModel.getSubjectDetailsList()
            subjectModel.getSubjectList()
            subjectModel.getMarkbookSubjects()
        }
    }

    @Test
    fun `Test profile flow processing is working`() = runBlocking {
        val profile = ProfileDTO(0, "", "", "", "", "", "", "", "0", "", "", "", "")
        every { profileModel.getProfile() } returns flow {
            delay(50)
            emit(profile)
            emit(profile)
            emit(profile)
        }
        profileInteractor = initInteractor()
        profileInteractor.subscribe(subscriber)
        delay(100)
        verify { subscriber.onProfileChange(any()) }
    }

    private fun <T> graduateFlow(delayAddition: Int, vararg t: T) = flow<List<T>> {
        var delayTime = 0L
        val array = mutableListOf<T>()

        t.forEach {
            delay(delayTime)
            emit(array.toList())
            array.add(it)
            delayTime += delayAddition
        }
        emit(array)
    }

    private fun initInteractor() =
        ProfileInteractor(profileModel, subjectModel, ConnectionManager())

    @After
    fun teardown() {
        unmockkAll()
    }
}