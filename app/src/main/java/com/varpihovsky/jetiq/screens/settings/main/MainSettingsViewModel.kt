package com.varpihovsky.jetiq.screens.settings.main

import androidx.lifecycle.viewModelScope
import com.varpihovsky.jetiq.back.model.ListModel
import com.varpihovsky.jetiq.back.model.MessagesModel
import com.varpihovsky.jetiq.back.model.ProfileModel
import com.varpihovsky.jetiq.back.model.SubjectModel
import com.varpihovsky.jetiq.system.JetIQViewModel
import com.varpihovsky.jetiq.system.navigation.NavigationDirections
import com.varpihovsky.jetiq.system.navigation.NavigationManager
import com.varpihovsky.jetiq.system.util.CoroutineDispatchers
import com.varpihovsky.jetiq.ui.appbar.AppbarManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainSettingsViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val navigationManager: NavigationManager,
    private val profileModel: ProfileModel,
    private val messagesModel: MessagesModel,
    private val subjectModel: SubjectModel,
    private val listModel: ListModel,
    appbarManager: AppbarManager,
) : JetIQViewModel(appbarManager, navigationManager) {
    fun onAboutClick() {
        navigationManager.manage(NavigationDirections.aboutSettings)
    }

    fun onLogoutClick() {
        viewModelScope.launch(dispatchers.IO) {
            profileModel.clearData()
            messagesModel.clearData()
            listModel.clear()
            subjectModel.removeAllSubjects()
            navigationManager.manage(NavigationDirections.authentication)
        }
    }
}