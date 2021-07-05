package com.varpihovsky.jetiq.di

import android.content.Context
import androidx.room.Room
import com.varpihovsky.jetiq.back.db.JetIQDatabase
import com.varpihovsky.jetiq.back.db.dao.*
import com.varpihovsky.jetiq.back.db.managers.*
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
    fun provideContactDatabaseManager(contactDAO: ContactDAO) = ContactDatabaseManager(contactDAO)

    @Provides
    @Singleton
    fun provideMessageDatabaseManager(messageDAO: MessageDAO) = MessageDatabaseManager(messageDAO)

    @Provides
    @Singleton
    fun provideSubjectDatabaseManager(subjectDAO: SubjectDAO) = SubjectDatabaseManager(subjectDAO)

    @Provides
    @Singleton
    fun provideSubjectDetailsDatabaseManager(subjectDetailsDAO: SubjectDetailsDAO) =
        SubjectDetailsDatabaseManager(subjectDetailsDAO)

    @Provides
    @Singleton
    fun provideProfileDatabaseManager(profileDAO: ProfileDAO) = ProfileDatabaseManager(profileDAO)

    @Provides
    @Singleton
    fun provideConfidentialDatabaseManager(confidentialDAO: ConfidentialDAO) =
        ConfidentialDatabaseManager(confidentialDAO)

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
        )
            .fallbackToDestructiveMigration()
            .build()
}