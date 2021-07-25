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

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.varpihovsky.core.exceptions.ExceptionEventManager
import com.varpihovsky.core_db.dao.*
import com.varpihovsky.core_network.managers.JetIQListManager
import com.varpihovsky.core_network.managers.JetIQSubjectManager
import com.varpihovsky.core_repo.RepoSingletonModule.USER_DATA_STORE
import com.varpihovsky.core_repo.repo.ListRepo
import com.varpihovsky.core_repo.repo.ProfileRepo
import com.varpihovsky.core_repo.repo.SubjectRepo
import com.varpihovsky.core_repo.repo.UserPreferencesRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Named

@Module
@InstallIn(ViewModelComponent::class)
object RepoViewModelModule {
    @Provides
    @ViewModelScoped
    fun provideListRepo(
        jetIQListManager: JetIQListManager,
        contactDAO: ContactDAO,
        exceptionEventManager: ExceptionEventManager
    ) = ListRepo(
        jetIQListManager,
        contactDAO,
        exceptionEventManager
    )

    @Provides
    @ViewModelScoped
    fun provideSubjectRepo(
        subjectDAO: SubjectDAO,
        subjectDetailsDAO: SubjectDetailsDAO,
        jetIQSubjectManager: JetIQSubjectManager,
        confidentialDAO: ConfidentialDAO,
        profileDAO: ProfileDAO,
        exceptionEventManager: ExceptionEventManager,
        profileRepo: ProfileRepo
    ) = SubjectRepo(
        subjectDAO,
        subjectDetailsDAO,
        jetIQSubjectManager,
        confidentialDAO,
        profileDAO,
        exceptionEventManager,
        profileRepo
    )

    @Provides
    @ViewModelScoped
    fun provideUserPreferencesRepo(
        @Named(USER_DATA_STORE) dataStore: DataStore<Preferences>
    ) = UserPreferencesRepo(dataStore)
}