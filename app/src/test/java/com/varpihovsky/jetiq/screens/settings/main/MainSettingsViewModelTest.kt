package com.varpihovsky.jetiq.screens.settings.main

import com.varpihovsky.jetiq.back.model.ListModel
import com.varpihovsky.jetiq.back.model.MessagesModel
import com.varpihovsky.jetiq.back.model.ProfileModel
import com.varpihovsky.jetiq.back.model.SubjectModel
import com.varpihovsky.jetiq.system.navigation.NavigationDirections
import com.varpihovsky.jetiq.testCore.ViewModelTest
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifyAll
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test

class MainSettingsViewModelTest : ViewModelTest() {
    private lateinit var mainSettingsViewModel: MainSettingsViewModel

    private val profileModel: ProfileModel = mockk(relaxed = true)
    private val messagesModel: MessagesModel = mockk(relaxed = true)
    private val subjectModel: SubjectModel = mockk(relaxed = true)
    private val listModel: ListModel = mockk(relaxed = true)

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
            profileModel.clearData()
            messagesModel.clearData()
            listModel.clear()
            subjectModel.removeAllSubjects()
        }
    }
}