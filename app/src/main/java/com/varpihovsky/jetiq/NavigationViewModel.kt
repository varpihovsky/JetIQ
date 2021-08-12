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
package com.varpihovsky.jetiq

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.varpihovsky.core.lifecycle.ViewModel
import com.varpihovsky.core.util.CoroutineDispatchers
import com.varpihovsky.core_nav.main.EntryType
import com.varpihovsky.core_nav.main.JetNav
import com.varpihovsky.core_nav.main.NavigationController
import com.varpihovsky.core_nav.main.NavigationEntry
import com.varpihovsky.core_nav.navigation.BottomNavigationItem
import com.varpihovsky.core_nav.navigation.BottomNavigationItemFactory
import com.varpihovsky.core_nav.navigation.NavigationCommand
import com.varpihovsky.core_nav.navigation.NavigationDirections
import com.varpihovsky.core_repo.repo.ProfileRepo
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class NavigationViewModel(
    private val dispatchers: CoroutineDispatchers,
    private val profileModel: ProfileRepo,
) : ViewModel() {
    val data by lazy { Data() }

    private val isNavbarShown = mutableStateOf(false)
    private val selectedNavbarEntry: MutableState<BottomNavigationItem> =
        mutableStateOf(BottomNavigationItem.ProfileItem)

    inner class Data {
        val isNavbarShown: State<Boolean> = this@NavigationViewModel.isNavbarShown
        val selectedNavbarEntry: State<BottomNavigationItem> =
            this@NavigationViewModel.selectedNavbarEntry
    }

    fun getStartDestination(): String =
        runBlocking(context = dispatchers.IO) {
            if (profileModel.getConfidential().firstOrNull() != null) {
                if (isNavbarShouldBeShown()) {
                    viewModelScope.launch { isNavbarShown.value = true }
                }
                return@runBlocking NavigationDirections.profile.destination
            }
            return@runBlocking NavigationDirections.authentication.destination
        }

    private fun isNavbarShouldBeShown() =
        JetNav.getControllerOrNull()
            ?.getCurrentDestination() == NavigationDirections.profile.destination
                || JetNav.getControllerOrNull() == null

    @ExperimentalAnimationApi
    fun onDestinationChange(direction: NavigationEntry) {
        isNavbarShown.value = direction.type is EntryType.Main
    }

    @ExperimentalAnimationApi
    fun onBottomBarButtonClick(
        direction: NavigationCommand,
        navigationController: NavigationController
    ) {
        navigationController.manage(direction.destination)
        BottomNavigationItemFactory.create(direction)?.let {
            viewModelScope.launch { selectedNavbarEntry.value = it }
        }
    }
}