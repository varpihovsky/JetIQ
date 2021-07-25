package com.varpihovsky.jetiq

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

import com.varpihovsky.core_nav.navigation.BottomNavigationItem
import com.varpihovsky.core_nav.navigation.NavigationDirections
import com.varpihovsky.core_repo.repo.ProfileRepo
import com.varpihovsky.jetiq.testCore.ViewModelTest
import com.varpihovsky.repo_data.Confidential
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import org.junit.Before
import org.junit.Test

class NavigationViewModelTest : ViewModelTest() {
    private lateinit var sharedViewModel: NavigationViewModel
    private val profileModel: ProfileRepo = mockk()

    @ExperimentalCoroutinesApi
    @Before
    override fun setup() {
        super.setup()

        sharedViewModel = NavigationViewModel(
            viewModelDispatchers,
            profileModel
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
    fun `Test bottom navbar click`() {
        sharedViewModel.onBottomBarButtonClick(NavigationDirections.profile, navigationController)
        verify(exactly = 1) { navigationController.manage(NavigationDirections.profile.destination) }
        assertEquals(
            BottomNavigationItem.ProfileItem,
            sharedViewModel.data.selectedNavbarEntry.value
        )
    }
}