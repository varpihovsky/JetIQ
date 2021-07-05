package com.varpihovsky.jetiq.di

import com.varpihovsky.jetiq.back.api.managers.JetIQListManager
import com.varpihovsky.jetiq.back.api.managers.JetIQMessageManager
import com.varpihovsky.jetiq.back.api.managers.JetIQProfileManager
import com.varpihovsky.jetiq.back.api.managers.JetIQSubjectManager
import com.varpihovsky.jetiq.back.db.managers.*
import com.varpihovsky.jetiq.back.model.ListModel
import com.varpihovsky.jetiq.back.model.MessagesModel
import com.varpihovsky.jetiq.back.model.ProfileModel
import com.varpihovsky.jetiq.back.model.SubjectModel
import com.varpihovsky.jetiq.system.ConnectionManager
import com.varpihovsky.jetiq.system.navigation.NavigationManager
import com.varpihovsky.jetiq.ui.appbar.AppbarManager
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
    fun provideAppbarManager() = AppbarManager()

    @Provides
    @Singleton
    fun provideMessagesModel(
        jetIQMessageManager: JetIQMessageManager,
        messageDatabaseManager: MessageDatabaseManager,
        confidentialDatabaseManager: ConfidentialDatabaseManager,
        profileDatabaseManager: ProfileDatabaseManager
    ) = MessagesModel(
        jetIQMessageManager,
        messageDatabaseManager,
        confidentialDatabaseManager,
        profileDatabaseManager
    )

    @Provides
    @Singleton
    fun provideListModel(
        contactDatabaseManager: ContactDatabaseManager,
        jetIQListManager: JetIQListManager
    ) = ListModel(jetIQListManager, contactDatabaseManager)

    @Provides
    @Singleton
    fun provideSubjectModel(
        confidentialDatabaseManager: ConfidentialDatabaseManager,
        subjectDatabaseManager: SubjectDatabaseManager,
        subjectDetailsDatabaseManager: SubjectDetailsDatabaseManager,
        jetIQSubjectManager: JetIQSubjectManager,
        profileDatabaseManager: ProfileDatabaseManager
    ) = SubjectModel(
        confidentialDatabaseManager,
        subjectDatabaseManager,
        subjectDetailsDatabaseManager,
        jetIQSubjectManager,
        profileDatabaseManager
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