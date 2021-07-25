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

import com.varpihovsky.core.exceptions.ExceptionEventManager
import com.varpihovsky.core_db.dao.ConfidentialDAO
import com.varpihovsky.core_db.dao.MessageDAO
import com.varpihovsky.core_db.dao.ProfileDAO
import com.varpihovsky.core_network.managers.JetIQMessageManager
import com.varpihovsky.core_network.managers.JetIQProfileManager
import com.varpihovsky.core_repo.repo.MessagesRepo
import com.varpihovsky.core_repo.repo.ProfileRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepoSingletonModule {
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
}