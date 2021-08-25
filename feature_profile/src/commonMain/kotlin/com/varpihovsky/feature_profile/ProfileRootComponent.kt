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
package com.varpihovsky.feature_profile

import com.arkivanov.decompose.RouterState
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.varpihovsky.core.log.i
import com.varpihovsky.core_lifecycle.JetIQComponentContext
import com.varpihovsky.core_lifecycle.childContext
import com.varpihovsky.feature_profile.profile.ProfileComponent
import com.varpihovsky.feature_profile.subjects.markbook.MarkbookSubjectComponent
import com.varpihovsky.feature_profile.subjects.success.SuccessSubjectComponent

class ProfileRootComponent(
    private val jetIQComponentContext: JetIQComponentContext
) : JetIQComponentContext by jetIQComponentContext, ProfileNavigationManager {
    internal val detailsRouterState: Value<RouterState<ProfileDetailsRouter.Config, DetailsChild>> by lazy { detailsRouter.routerState }
    internal val isMultiPane: Value<Boolean> by lazy { _isMultiPane }
    internal val profileComponent = ProfileComponent(
        DefaultProfileComponentContext(childContext("ProfileComponent"), this)
    )

    private val detailsRouter = ProfileDetailsRouter(jetIQComponentContext, this)
    private val _isMultiPane = MutableValue(false)

    init {
        backPressedDispatcher.register {
            i(detailsRouter.isShown().toString())
            if (!detailsRouter.isShown()) {
                false
            } else {
                detailsRouter.clear()
                true
            }
        }
    }

    override fun navigateToMarkbook(id: Int) {
        detailsRouter.navigateToMarkbook(id)
    }

    override fun navigateToSuccess(id: Int) {
        detailsRouter.navigateToSuccess(id)
    }

    override fun navigateToSettings() {
        mainNavigationController.navigateToSettings()
    }

    internal fun setMultiPane(isMultiPane: Boolean) {
        _isMultiPane.value = isMultiPane
    }

    internal sealed class DetailsChild {
        object None : DetailsChild()
        class Markbook(val component: MarkbookSubjectComponent) : DetailsChild()
        class Success(val component: SuccessSubjectComponent) : DetailsChild()
    }
}