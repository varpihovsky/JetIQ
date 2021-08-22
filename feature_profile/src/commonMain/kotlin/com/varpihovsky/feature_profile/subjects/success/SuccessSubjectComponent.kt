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
package com.varpihovsky.feature_profile.subjects.success

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.varpihovsky.core_repo.repo.SubjectRepo
import com.varpihovsky.feature_profile.ProfileComponentContext
import com.varpihovsky.jetiqApi.data.Subject
import com.varpihovsky.jetiqApi.data.SubjectDetails
import kotlinx.coroutines.flow.Flow
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

internal class SuccessSubjectComponent(
    profileComponentContext: ProfileComponentContext,
    subjectId: Int,
    isInFullScreen: Boolean
) : ProfileComponentContext by profileComponentContext, KoinComponent {
    val subjectDetails: Flow<SubjectDetails?> = get<SubjectRepo>().getDetailsById(subjectId)
    val subject: Flow<Subject?> = get<SubjectRepo>().getSubjectById(subjectId)
    val isInFullScreen: Value<Boolean> by lazy { _isInFullScreen }

    private val _isInFullScreen = MutableValue(isInFullScreen)

    fun setFullScreen(isInFullScreen: Boolean) {
        _isInFullScreen.value = isInFullScreen
    }
}
