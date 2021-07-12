package com.varpihovsky.jetiq.screens.settings.main

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
            navigationManager,
            profileModel,
            messagesModel,
            subjectModel,
            listModel,
            appbarManager
        )
    }

    @Test
    fun `Test navigates to about settings on about button click`() {
        mainSettingsViewModel.onAboutClick()
        verify { navigationManager.manage(NavigationDirections.aboutSettings) }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `Test navigates to auth screen on logout button click`() = runBlockingTest {
        mainSettingsViewModel.onLogoutClick()
        verify { navigationManager.manage(NavigationDirections.authentication) }
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