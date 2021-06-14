package com.varpihovsky.jetiq.di

import com.varpihovsky.jetiq.back.api.managers.JetIQProfileManager
import com.varpihovsky.jetiq.back.api.managers.JetIQSubjectManager
import com.varpihovsky.jetiq.back.db.managers.ConfidentialDatabaseManager
import com.varpihovsky.jetiq.back.db.managers.ProfileDatabaseManager
import com.varpihovsky.jetiq.back.db.managers.SubjectDatabaseManager
import com.varpihovsky.jetiq.back.db.managers.SubjectDetailsDatabaseManager
import com.varpihovsky.jetiq.back.model.ProfileModel
import com.varpihovsky.jetiq.back.model.SubjectModel
import com.varpihovsky.jetiq.system.ConnectionManager
import com.varpihovsky.jetiq.system.navigation.NavigationManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SingletonModule {
    @Provides
    fun provideConnectionManager() = ConnectionManager()

    @Provides
    @Singleton
    fun provideNavigationManager() = NavigationManager()

    @Provides
    @Singleton
    fun provideSubjectModel(
        confidentialDatabaseManager: ConfidentialDatabaseManager,
        subjectDatabaseManager: SubjectDatabaseManager,
        subjectDetailsDatabaseManager: SubjectDetailsDatabaseManager,
        jetIQSubjectManager: JetIQSubjectManager
    ) = SubjectModel(
        confidentialDatabaseManager,
        subjectDatabaseManager,
        subjectDetailsDatabaseManager,
        jetIQSubjectManager
    )

    @Provides
    @Singleton
    fun provideProfileModel(
        profileDatabaseManager: ProfileDatabaseManager,
        confidentialDatabaseManager: ConfidentialDatabaseManager,
        jetIQProfileManager: JetIQProfileManager
    ) = ProfileModel(
        profileDatabaseManager,
        confidentialDatabaseManager,
        jetIQProfileManager
    )
}