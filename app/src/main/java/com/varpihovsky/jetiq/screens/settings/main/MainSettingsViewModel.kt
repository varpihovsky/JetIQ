package com.varpihovsky.jetiq.screens.settings.main

import androidx.lifecycle.viewModelScope
import com.varpihovsky.core.navigation.NavigationDirections
import com.varpihovsky.core.util.CoroutineDispatchers
import com.varpihovsky.core_nav.main.NavigationController
import com.varpihovsky.core_repo.repo.ListRepo
import com.varpihovsky.core_repo.repo.MessagesRepo
import com.varpihovsky.core_repo.repo.ProfileRepo
import com.varpihovsky.core_repo.repo.SubjectRepo
import com.varpihovsky.jetiq.appbar.AppbarManager
import com.varpihovsky.jetiq.screens.JetIQViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainSettingsViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val navigationManager: NavigationController,
    private val profileModel: ProfileRepo,
    private val messagesModel: MessagesRepo,
    private val subjectModel: SubjectRepo,
    private val listModel: ListRepo,
    appbarManager: AppbarManager,
) : JetIQViewModel(appbarManager, navigationManager) {
    fun onAboutClick() {
        navigationManager.manage(NavigationDirections.aboutSettings.destination)
    }

    fun onLogoutClick() {
        viewModelScope.launch(dispatchers.IO) {
            profileModel.clear()
            messagesModel.clear()
            listModel.clear()
            subjectModel.clear()
            navigationManager.manage(NavigationDirections.authentication.destination)
        }
    }
}