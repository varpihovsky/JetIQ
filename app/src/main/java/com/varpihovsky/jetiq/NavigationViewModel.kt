package com.varpihovsky.jetiq

import androidx.compose.runtime.State
import com.varpihovsky.core.navigation.*
import com.varpihovsky.core.util.CoroutineDispatchers
import com.varpihovsky.core.util.ThreadSafeMutableState
import com.varpihovsky.core_repo.repo.ProfileRepo
import com.varpihovsky.jetiq.appbar.AppbarManager
import com.varpihovsky.jetiq.screens.JetIQViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class NavigationViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val profileModel: ProfileRepo,
    private val navigationManager: NavigationManager,
    appbarManager: AppbarManager
) : JetIQViewModel(appbarManager, navigationManager) {
    val data by lazy { Data() }

    private val isNavbarShown = mutableStateOf(false)
    private val selectedNavbarEntry: ThreadSafeMutableState<BottomNavigationItem> =
        mutableStateOf(BottomNavigationItem.ProfileItem)
    private var currentDestination: String? = null

    inner class Data {
        val isNavbarShown: State<Boolean> = this@NavigationViewModel.isNavbarShown
        val selectedNavbarEntry: State<BottomNavigationItem> =
            this@NavigationViewModel.selectedNavbarEntry
    }

    fun getStartDestination(): String =
        runBlocking(context = dispatchers.IO) {
            if (profileModel.getConfidential().firstOrNull() != null) {
                isNavbarShown.value = true
                return@runBlocking NavigationDirections.profile.destination
            }
            return@runBlocking NavigationDirections.authentication.destination
        }

    fun onDestinationChange(direction: String) {
        isNavbarShown.value =
            direction == NavigationDirections.profile.destination || direction == NavigationDirections.messages.destination
        currentDestination = direction
    }

    fun getCurrentDestination() = currentDestination

    fun onBottomBarButtonClick(direction: NavigationCommand) {
        navigationManager.manage(direction)
        BottomNavigationItemFactory.create(direction)?.let {
            selectedNavbarEntry.value = it
        }
    }
}