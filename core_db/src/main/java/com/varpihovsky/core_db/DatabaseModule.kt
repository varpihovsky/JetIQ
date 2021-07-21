package com.varpihovsky.core_db

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
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideContactDAO(jetIQDatabase: JetIQDatabase) = jetIQDatabase.contactDAO()

    @Provides
    @Singleton
    fun provideMessageDAO(jetIQDatabase: JetIQDatabase) = jetIQDatabase.messageDAO()

    @Provides
    @Singleton
    fun provideSubjectDAO(jetIQDatabase: JetIQDatabase) = jetIQDatabase.subjectDAO()

    @Provides
    @Singleton
    fun provideSubjectDetailsDAO(jetIQDatabase: JetIQDatabase) = jetIQDatabase.subjectDetailsDAO()

    @Provides
    @Singleton
    fun provideConfidentialDAO(jetIQDatabase: JetIQDatabase) = jetIQDatabase.confidentialDAO()

    @Provides
    @Singleton
    fun provideProfileDAO(jetIQDatabase: JetIQDatabase) = jetIQDatabase.profileDAO()


    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context) =
        Room.databaseBuilder(
            context,
            JetIQDatabase::class.java,
            "JetIQ_Student_Neon"
        ).fallbackToDestructiveMigration()
            .build()
}