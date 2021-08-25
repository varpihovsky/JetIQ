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
package com.varpihovsky.feature_profile.profile

import androidx.compose.foundation.ScrollState
import androidx.compose.runtime.State
import com.varpihovsky.core.coroutines.runBlocking
import com.varpihovsky.core.util.CoroutineDispatchers
import com.varpihovsky.core_lifecycle.childContext
import com.varpihovsky.core_repo.repo.UserPreferencesRepo
import com.varpihovsky.feature_profile.ProfileComponentContext
import com.varpihovsky.ui_data.dto.UIProfileDTO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

internal class ProfileComponent(
    profileComponentContext: ProfileComponentContext,
) : ProfileComponentContext by profileComponentContext, KoinComponent {
    lateinit var markbookComponent: SubjectsComponent
    lateinit var successSubjectsComponent: SubjectsComponent

    val scrollState = ScrollState(0)
    val isLoading: State<Boolean>
        get() = profileInteractor.isLoading

    lateinit var profile: StateFlow<UIProfileDTO>

    private val profileInteractor: ProfileInteractor by inject()
    private val dispatchers: CoroutineDispatchers by inject()
    private val userPreferencesRepo: UserPreferencesRepo by inject()

    private val scope = CoroutineScope(Dispatchers.Main)

    val expandButtonLocation = userPreferencesRepo.flow
        .map { it.profileListExpandButtonLocation }
        .distinctUntilChanged()

    init {
        runBlocking {
            scope.launch(dispatchers.IO) {
                profile = profileInteractor.profileData.distinctUntilChanged().stateIn(
                    scope, SharingStarted.Eagerly, UIProfileDTO()
                )

                markbookComponent = SubjectsComponent(
                    childContext("MarkbookComponent"),
                    profileInteractor.markbookData.map { it.first }.distinctUntilChanged(),
                    profileInteractor.markbookData.map { it.second }.distinctUntilChanged()
                        .stateIn(scope, SharingStarted.Eagerly, listOf()),
                    userPreferencesRepo.flow.map { it.markbookListType }.distinctUntilChanged(),
                ) { subject -> navigationManager.navigateToMarkbook(subject.id) }

                successSubjectsComponent = SubjectsComponent(
                    childContext("SuccessSubjectsComponent"),
                    profileInteractor.successData.map { it.first }.distinctUntilChanged(),
                    profileInteractor.successData.map { it.second }.distinctUntilChanged()
                        .stateIn(scope, SharingStarted.Eagerly, listOf()),
                    userPreferencesRepo.flow.map { it.successListType }.distinctUntilChanged()
                ) { subject -> navigationManager.navigateToSuccess(subject.id) }
            }.join()
        }
    }

    fun onRefresh() {
        scope.launch(dispatchers.IO) { profileInteractor.onRefresh() }
    }

    fun onSettingsClick() {
        navigationManager.navigateToSettings()
    }
}
