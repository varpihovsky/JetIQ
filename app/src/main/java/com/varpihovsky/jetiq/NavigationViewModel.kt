package com.varpihovsky.jetiq

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.varpihovsky.jetiq.back.model.ProfileModel
import com.varpihovsky.jetiq.system.navigation.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class NavigationViewModel @Inject constructor(
    private val profileModel: ProfileModel,
    private val navigationManager: NavigationManager
) : ViewModel() {
    val data by lazy { Data() }

    private val isNavbarShown = MutableLiveData(false)
    private val selectedNavbarEntry: MutableLiveData<BottomNavigationItem> =
        MutableLiveData(BottomNavigationItem.ProfileItem)
    private var currentDestination: String? = null

    inner class Data {
        val isNavbarShown: LiveData<Boolean> = this@NavigationViewModel.isNavbarShown
        val selectedNavbarEntry: LiveData<BottomNavigationItem> =
            this@NavigationViewModel.selectedNavbarEntry
    }

    fun getStartDestination(): String =
        runBlocking {
            try {
                Log.d("Application", profileModel.getConfidential().first().toString())
                isNavbarShown.value = true
                return@runBlocking NavigationDirections.profile.destination
            } catch (e: Exception) {
                return@runBlocking NavigationDirections.authentication.destination
            }
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