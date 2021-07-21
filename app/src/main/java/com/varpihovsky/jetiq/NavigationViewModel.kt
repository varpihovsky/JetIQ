package com.varpihovsky.jetiq

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

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.varpihovsky.core.navigation.*
import com.varpihovsky.core.util.CoroutineDispatchers
import com.varpihovsky.core_nav.main.EntryType
import com.varpihovsky.core_nav.main.NavigationController
import com.varpihovsky.core_nav.main.NavigationEntry
import com.varpihovsky.core_repo.repo.ProfileRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class NavigationViewModel @Inject constructor(
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
                viewModelScope.launch { isNavbarShown.value = true }
                return@runBlocking NavigationDirections.profile.destination
            }
            return@runBlocking NavigationDirections.authentication.destination
        }

    fun onDestinationChange(direction: NavigationEntry) {
        isNavbarShown.value = direction.type is EntryType.Main
    }

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