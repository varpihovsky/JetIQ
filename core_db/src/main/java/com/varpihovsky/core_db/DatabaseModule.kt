package com.varpihovsky.core_db

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