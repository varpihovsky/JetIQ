package com.varpihovsky.jetiq.screens.profile

import androidx.compose.foundation.ScrollState
import androidx.compose.runtime.State
import com.varpihovsky.core.Refreshable
import com.varpihovsky.core.appbar.AppbarManager
import com.varpihovsky.core.exceptions.ExceptionEventManager
import com.varpihovsky.core.navigation.NavigationDirections
import com.varpihovsky.core_nav.main.NavigationController
import com.varpihovsky.jetiq.screens.JetIQViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileInteractor: ProfileInteractor,
    private val navigationController: NavigationController,
    appbarManager: AppbarManager,
    exceptionEventManager: ExceptionEventManager,
) : JetIQViewModel(appbarManager, navigationController, exceptionEventManager), Refreshable {
    val data by lazy { Data() }
    val scrollState = ScrollState(0)
    override val isLoading: State<Boolean>
        get() = profileInteractor.isLoading

    val profile = profileInteractor.profileData

    val successMarksInfo = profileInteractor.successData.map { it.first }
    val successSubjects = profileInteractor.successData.map { it.second }

    val markbookMarksInfo = profileInteractor.markbookData.map { it.first }
    val markbookSubjects = profileInteractor.markbookData.map { it.second }

    private val successChecked = mutableStateOf(false)
    private val markbookChecked = mutableStateOf(false)

    inner class Data {
        val successChecked: State<Boolean> = this@ProfileViewModel.successChecked
        val markbookChecked: State<Boolean> = this@ProfileViewModel.markbookChecked
    }

    fun onSuccessToggle(checked: Boolean) {
        successChecked.value = checked
    }

    fun onMarkbookToggle(checked: Boolean) {
        markbookChecked.value = checked
    }

    override fun onRefresh() {
        profileInteractor.onRefresh()
    }

    fun onSettingsClick() {
        navigationController.manage(NavigationDirections.mainSettings.destination)
    }
}
