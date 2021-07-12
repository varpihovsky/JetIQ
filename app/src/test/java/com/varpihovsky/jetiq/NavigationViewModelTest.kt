package com.varpihovsky.jetiq

import com.varpihovsky.core.navigation.BottomNavigationItem
import com.varpihovsky.core.navigation.NavigationDirections
import com.varpihovsky.core_repo.repo.ProfileRepo
import com.varpihovsky.jetiq.testCore.ViewModelTest
import com.varpihovsky.repo_data.Confidential
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import org.junit.Before
import org.junit.Test


class NavigationViewModelTest : ViewModelTest() {
    private lateinit var sharedViewModel: NavigationViewModel
    private val profileModel: ProfileRepo = mockk()

    private val mainScreens = listOf(
        NavigationDirections.profile.destination,
        NavigationDirections.messages.destination
    )

    private val subScreens = listOf(
        NavigationDirections.authentication.destination,
        NavigationDirections.aboutSettings.destination,
        NavigationDirections.mainSettings.destination,
        NavigationDirections.newMessage.destination,
        NavigationDirections.contacts.destination,
    )

    @ExperimentalCoroutinesApi
    @Before
    override fun setup() {
        super.setup()

        sharedViewModel = NavigationViewModel(
            viewModelDispatchers,
            profileModel,
            navigationManager,
            appbarManager
        )
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
            emit(Confidential("someuser", "somepassword"))
        }
        assertEquals(
            NavigationDirections.profile.destination,
            sharedViewModel.getStartDestination()
        )
    }

    @Test
    fun `Test navbar visibility when it is main screen`() {
        mainScreens.forEach {
            sharedViewModel.onDestinationChange(it)
            assertEquals(sharedViewModel.getCurrentDestination(), it)
            assertTrue(sharedViewModel.data.isNavbarShown.value)
        }
    }

    @Test
    fun `Test navbar visibility when it is subScreen`() {
        subScreens.forEach {
            sharedViewModel.onDestinationChange(it)
            assertEquals(sharedViewModel.getCurrentDestination(), it)
            assertTrue(!sharedViewModel.data.isNavbarShown.value)
        }
    }

    @Test
    fun `Test bottom navbar click`() {
        sharedViewModel.onBottomBarButtonClick(NavigationDirections.profile)
        verify(exactly = 1) { navigationManager.manage(NavigationDirections.profile) }
        assertEquals(
            BottomNavigationItem.ProfileItem,
            sharedViewModel.data.selectedNavbarEntry.value
        )
    }
}