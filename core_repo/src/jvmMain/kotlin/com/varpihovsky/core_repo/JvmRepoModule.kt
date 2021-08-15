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
package com.varpihovsky.core_repo

import com.varpihovsky.core_repo.repo.PreferencesKeys
import com.varpihovsky.core_repo.repo.UserPreferencesRepo
import com.varpihovsky.repo_data.UserPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import org.koin.core.module.Module

internal actual fun Module.providePlatform() {
    single {
        // TODO: Replace with real repo. Better if it will be cross platform solution.
        object : UserPreferencesRepo {
            override val flow: Flow<UserPreferences> get() = emptyFlow()

            override suspend fun <T> set(key: PreferencesKeys, value: T) {}

            override suspend fun clear() {}

        }
    }
}
