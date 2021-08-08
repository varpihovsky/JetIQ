package com.varpihovsky.core_db

import androidx.room.Room
import org.koin.core.module.Module
import org.koin.dsl.module

//    @Provides
//    @Singleton
//    fun provideContactDAO(jetIQDatabase: JetIQDatabase) = jetIQDatabase.contactDAO()
//
//    @Provides
//    @Singleton
//    fun provideMessageDAO(jetIQDatabase: JetIQDatabase) = jetIQDatabase.messageDAO()
//
//    @Provides
//    @Singleton
//    fun provideSubjectDAO(jetIQDatabase: JetIQDatabase) = jetIQDatabase.subjectDAO()
//
//    @Provides
//    @Singleton
//    fun provideSubjectDetailsDAO(jetIQDatabase: JetIQDatabase) = jetIQDatabase.subjectDetailsDAO()
//
//    @Provides
//    @Singleton
//    fun provideConfidentialDAO(jetIQDatabase: JetIQDatabase) = jetIQDatabase.confidentialDAO()
//
//    @Provides
//    @Singleton
//    fun provideProfileDAO(jetIQDatabase: JetIQDatabase) = jetIQDatabase.profileDAO()
//

internal actual fun provideModule(): Module = module {
    single { get<JetIQDatabase>().contactDAO() }
    single { get<JetIQDatabase>().messageDAO() }
    single { get<JetIQDatabase>().subjectDAO() }
    single { get<JetIQDatabase>().subjectDetailsDAO() }
    single { get<JetIQDatabase>().confidentialDAO() }
    single { get<JetIQDatabase>().profileDAO() }

    single {
        Room.databaseBuilder(
            get(),
            JetIQDatabase::class.java,
            "JetIQ_Student_Neon"
        ).fallbackToDestructiveMigration().build()
    }
}