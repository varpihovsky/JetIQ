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

import com.varpihovsky.core.util.CoroutineDispatchers
import com.varpihovsky.core_repo.repo.ListRepo
import com.varpihovsky.core_repo.repo.MessagesRepo
import com.varpihovsky.core_repo.repo.ProfileRepo
import com.varpihovsky.core_repo.repo.SubjectRepo
import com.varpihovsky.feature_settings.SettingsComponentContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

internal class MainSettingsComponent(
    jetIQComponentContext: SettingsComponentContext
) : SettingsComponentContext by jetIQComponentContext, KoinComponent {
    private val dispatchers: CoroutineDispatchers by inject()
    private val profileRepo: ProfileRepo by inject()
    private val messagesRepo: MessagesRepo by inject()
    private val listRepo: ListRepo by inject()
    private val subjectRepo: SubjectRepo by inject()

    private val scope = CoroutineScope(Dispatchers.Main)

    fun onGeneralClick() {
        settingsNavigationHandler.navigateToGeneral()
    }

    fun onAboutClick() {
        settingsNavigationHandler.navigateToAbout()
    }

    fun onLogoutClick() {
        scope.launch(dispatchers.IO) {
            profileRepo.clear()
            messagesRepo.clear()
            listRepo.clear()
            subjectRepo.clear()
            settingsNavigationHandler.navigateToAuth()
        }
    }
}