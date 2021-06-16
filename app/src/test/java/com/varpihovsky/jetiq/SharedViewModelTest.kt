package com.varpihovsky.jetiq

import com.varpihovsky.jetiq.back.dto.Confidential
import com.varpihovsky.jetiq.back.model.ProfileModel
import com.varpihovsky.jetiq.system.navigation.NavigationDirections
import io.mockk.every
import io.mockk.mockk
import io.mockk.unmockkAll
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.flow.flow
import org.junit.After
import org.junit.Before
import org.junit.Test

class SharedViewModelTest {
    private lateinit var sharedViewModel: SharedViewModel
    private val profileModel: ProfileModel = mockk()

    @Before
    fun setup() {
        sharedViewModel = SharedViewModel(profileModel)
    }

    @Test
    fun `Test getting main start destination when there is no user confidential in db`() {
        every { profileModel.getConfidential() } returns flow { }
        assertEquals(
            NavigationDirections.authentication.destination,
            sharedViewModel.getStartDestination()
        )
    }

    @Test
    fun `Test get main start destination where there is user confidential in db`() {
        every { profileModel.getConfidential() } returns flow {
            emit(
                Confidential(
                    "someuser",
                    "somepassword"
                )
            )
        }
        assertEquals(
            NavigationDirections.profile.destination,
            sharedViewModel.getStartDestination()
        )
    }

    @After
    fun teardown() {
        unmockkAll()
    }
}