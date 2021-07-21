package com.varpihovsky.jetiq.screens.settings.main

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

import com.varpihovsky.core.navigation.NavigationDirections
import com.varpihovsky.core_repo.repo.ListRepo
import com.varpihovsky.core_repo.repo.MessagesRepo
import com.varpihovsky.core_repo.repo.ProfileRepo
import com.varpihovsky.core_repo.repo.SubjectRepo
import com.varpihovsky.jetiq.testCore.ViewModelTest
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifyAll
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test

class MainSettingsViewModelTest : ViewModelTest() {
    private lateinit var mainSettingsViewModel: MainSettingsViewModel

    private val profileModel: ProfileRepo = mockk(relaxed = true)
    private val messagesModel: MessagesRepo = mockk(relaxed = true)
    private val subjectModel: SubjectRepo = mockk(relaxed = true)
    private val listModel: ListRepo = mockk(relaxed = true)

    @ExperimentalCoroutinesApi
    override fun setup() {
        super.setup()

        mainSettingsViewModel = MainSettingsViewModel(
            viewModelDispatchers,
            navigationController,
            profileModel,
            messagesModel,
            subjectModel,
            listModel,
            appbarManager,
            exceptionEventManager
        )
    }

    @Test
    fun `Test navigates to about settings on about button click`() {
        mainSettingsViewModel.onAboutClick()
        verify { navigationController.manage(NavigationDirections.aboutSettings.destination) }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `Test navigates to auth screen on logout button click`() = runBlockingTest {
        mainSettingsViewModel.onLogoutClick()
        verify { navigationController.manage(NavigationDirections.authentication.destination) }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `Test data clears on logout button click`() = runBlockingTest {
        mainSettingsViewModel.onLogoutClick()
        verifyAll {
            profileModel.clear()
            messagesModel.clear()
            listModel.clear()
            subjectModel.clear()
        }
    }
}