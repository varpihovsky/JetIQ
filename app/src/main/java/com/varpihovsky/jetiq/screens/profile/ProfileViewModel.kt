package com.varpihovsky.jetiq.screens.profile

import android.util.Log
import androidx.compose.foundation.ScrollState
import androidx.compose.runtime.State
import androidx.lifecycle.viewModelScope
import com.varpihovsky.core.Refreshable
import com.varpihovsky.core.appbar.AppbarManager
import com.varpihovsky.core.exceptions.ViewModelWithException
import com.varpihovsky.core.navigation.NavigationDirections
import com.varpihovsky.core_nav.main.NavigationController
import com.varpihovsky.jetiq.screens.JetIQViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileInteractor: ProfileInteractor,
    private val navigationController: NavigationController,
    appbarManager: AppbarManager,
) : JetIQViewModel(appbarManager, navigationController),
    ViewModelWithException, Refreshable {
    val data by lazy { Data() }
    val scrollState = ScrollState(0)
    override val isLoading: State<Boolean>
        get() = profileInteractor.isLoading

    private var scrollChange = 0

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

    init {
        profileInteractor._exceptions = exceptions
    }

    fun onSuccessToggle(checked: Boolean) {
        successChecked.value = checked
    }

    fun onMarkbookToggle(checked: Boolean) {
        markbookChecked.value = checked
    }

    override fun onRefresh() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                profileInteractor.onRefresh()
            } catch (e: RuntimeException) {
                exceptions.value = e
                Log.d("Application", Log.getStackTraceString(e))
            }
        }
    }

    fun onSettingsClick() {
        navigationController.manage(NavigationDirections.mainSettings.destination)
    }
}
