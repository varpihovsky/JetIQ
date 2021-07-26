package com.varpihovsky.core_repo

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

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.varpihovsky.core.exceptions.ExceptionEventManager
import com.varpihovsky.core_db.dao.ConfidentialDAO
import com.varpihovsky.core_db.dao.MessageDAO
import com.varpihovsky.core_db.dao.ProfileDAO
import com.varpihovsky.core_network.managers.JetIQMessageManager
import com.varpihovsky.core_network.managers.JetIQProfileManager
import com.varpihovsky.core_repo.repo.MessagesRepo
import com.varpihovsky.core_repo.repo.ProfileRepo
import com.varpihovsky.core_repo.repo.UserPreferencesRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepoSingletonModule {
    private const val USER_DATA_STORE = "UserDataSource"

    @Provides
    @Singleton
    fun provideMessagesRepo(
        jetIQMessageManager: JetIQMessageManager,
        messageDAO: MessageDAO,
        confidentialDAO: ConfidentialDAO,
        profileDAO: ProfileDAO,
        exceptionEventManager: ExceptionEventManager
    ) = MessagesRepo(
        jetIQMessageManager,
        messageDAO,
        confidentialDAO,
        profileDAO,
        exceptionEventManager
    )

    @Provides
    @Singleton
    fun provideProfileRepo(
        profileDAO: ProfileDAO,
        confidentialDAO: ConfidentialDAO,
        jetIQProfileManager: JetIQProfileManager,
        exceptionEventManager: ExceptionEventManager
    ) = ProfileRepo(
        profileDAO,
        confidentialDAO,
        jetIQProfileManager,
        exceptionEventManager
    )

    @Provides
    @Singleton
    fun provideUserPreferencesRepo(
        @Named(USER_DATA_STORE) dataStore: DataStore<Preferences>,
        exceptionEventManager: ExceptionEventManager
    ) = UserPreferencesRepo(dataStore, exceptionEventManager)

    @Named(USER_DATA_STORE)
    @Provides
    @Singleton
    fun provideUserDataStore(@ApplicationContext context: Context): DataStore<Preferences> =
        PreferenceDataStoreFactory.create { context.preferencesDataStoreFile(USER_DATA_STORE) }
}