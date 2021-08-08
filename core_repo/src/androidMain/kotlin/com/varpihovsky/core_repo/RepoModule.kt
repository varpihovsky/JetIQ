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

import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.preferencesDataStoreFile
import com.varpihovsky.core_repo.repo.providePreferences
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module

private const val USER_DATA_STORE = "UserDataSource"

internal actual fun Module.providePlatform() {
    factory { providePreferences(get(), get()) }

    single {
        PreferenceDataStoreFactory.create {
            androidContext().preferencesDataStoreFile(
                USER_DATA_STORE
            )
        }
    }
}
