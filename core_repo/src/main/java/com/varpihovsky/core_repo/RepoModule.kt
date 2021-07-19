package com.varpihovsky.core_repo

import com.varpihovsky.core.exceptions.ExceptionEventManager
import com.varpihovsky.core_db.dao.*
import com.varpihovsky.core_network.managers.JetIQListManager
import com.varpihovsky.core_network.managers.JetIQMessageManager
import com.varpihovsky.core_network.managers.JetIQProfileManager
import com.varpihovsky.core_network.managers.JetIQSubjectManager
import com.varpihovsky.core_repo.repo.ListRepo
import com.varpihovsky.core_repo.repo.MessagesRepo
import com.varpihovsky.core_repo.repo.ProfileRepo
import com.varpihovsky.core_repo.repo.SubjectRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepoModule {
    @Provides
    @Singleton
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
    fun provideSubjectRepo(
        subjectDAO: SubjectDAO,
        subjectDetailsDAO: SubjectDetailsDAO,
        jetIQSubjectManager: JetIQSubjectManager,
        confidentialDAO: ConfidentialDAO,
        profileDAO: ProfileDAO,
        exceptionEventManager: ExceptionEventManager
    ) = SubjectRepo(
        subjectDAO,
        subjectDetailsDAO,
        jetIQSubjectManager,
        confidentialDAO,
        profileDAO,
        exceptionEventManager
    )
}