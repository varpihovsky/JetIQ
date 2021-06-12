package com.varpihovsky.jetiq.di

import android.content.Context
import androidx.room.Room
import com.varpihovsky.jetiq.back.api.JetIQApi
import com.varpihovsky.jetiq.back.db.JetIQDatabase
import com.varpihovsky.jetiq.back.db.dao.ProfileDAO
import com.varpihovsky.jetiq.back.db.managers.ProfileDatabaseManager
import com.varpihovsky.jetiq.back.model.ProfileModel
import com.varpihovsky.jetiq.system.navigation.NavigationManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SingletonModule {

    @Provides
    @Singleton
    fun provideNavigationManager() = NavigationManager()

    @Provides
    @Singleton
    fun provideProfileModel(jetIQApi: JetIQApi, profileDatabaseManager: ProfileDatabaseManager) =
        ProfileModel(jetIQApi, profileDatabaseManager)

    @Provides
    @Singleton
    fun provideProfileDatabaseManager(profileDAO: ProfileDAO) = ProfileDatabaseManager(profileDAO)

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
        ).build()

    @Provides
    @Singleton
    fun provideJetIQApi(retrofit: Retrofit) = retrofit.create(JetIQApi::class.java) as JetIQApi

    @Provides
    @Singleton
    fun provideRetrofit() = Retrofit
        .Builder()
        .baseUrl("https://iq.vntu.edu.ua/b04213/curriculum/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}