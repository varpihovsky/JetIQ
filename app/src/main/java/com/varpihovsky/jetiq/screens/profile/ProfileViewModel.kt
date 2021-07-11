package com.varpihovsky.jetiq.screens.profile

import android.util.Log
import androidx.compose.foundation.ScrollState
import androidx.compose.runtime.State
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.varpihovsky.jetiq.system.JetIQViewModel
import com.varpihovsky.jetiq.system.exceptions.ViewModelWithException
import com.varpihovsky.jetiq.system.navigation.NavigationDirections
import com.varpihovsky.jetiq.system.navigation.NavigationManager
import com.varpihovsky.jetiq.ui.appbar.AppbarManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileInteractor: ProfileInteractor,
    private val navigationManager: NavigationManager,
    appbarManager: AppbarManager,
) : JetIQViewModel(appbarManager, navigationManager),
    ViewModelWithException {
    val data by lazy { Data() }
    val scrollState = ScrollState(0)
    val isLoading: LiveData<Boolean>
        get() = profileInteractor.isLoading
    override val exceptions: MutableStateFlow<Exception?> = MutableStateFlow(null)

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

    fun onSuccessToggle(checked: Boolean) {
        successChecked.value = checked
    }

    fun onMarkbookToggle(checked: Boolean) {
        markbookChecked.value = checked
    }

    fun onRefresh() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                profileInteractor.refresh()
            } catch (e: RuntimeException) {
                exceptions.value = e
                Log.d("Application", Log.getStackTraceString(e))
            }
        }
    }

    fun onSettingsClick() {
        navigationManager.manage(NavigationDirections.mainSettings)
    }
}
