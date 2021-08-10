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
package com.varpihovsky.feature_settings.main

import com.varpihovsky.core.appbar.AppbarManager
import com.varpihovsky.core.exceptions.ExceptionEventManager
import com.varpihovsky.core.lifecycle.viewModelScope
import com.varpihovsky.core.util.CoroutineDispatchers
import com.varpihovsky.core_lifecycle.JetIQViewModel
import com.varpihovsky.core_nav.main.NavigationController
import com.varpihovsky.core_nav.navigation.NavigationDirections
import com.varpihovsky.core_repo.repo.ListRepo
import com.varpihovsky.core_repo.repo.MessagesRepo
import com.varpihovsky.core_repo.repo.ProfileRepo
import com.varpihovsky.core_repo.repo.SubjectRepo
import kotlinx.coroutines.launch

class MainSettingsViewModel(
    private val dispatchers: CoroutineDispatchers,
    private val navigationManager: NavigationController,
    private val profileModel: ProfileRepo,
    private val messagesModel: MessagesRepo,
    private val subjectModel: SubjectRepo,
    private val listModel: ListRepo,
    appbarManager: AppbarManager,
    exceptionEventManager: ExceptionEventManager,
) : JetIQViewModel(appbarManager, navigationManager, exceptionEventManager) {
    fun onGeneralClick() {
        navigationManager.manage(NavigationDirections.generalSettings.destination)
    }

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