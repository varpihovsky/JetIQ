package com.varpihovsky.jetiq.di

import com.varpihovsky.jetiq.back.api.JetIQApi
import com.varpihovsky.jetiq.back.api.managers.JetIQMessageManager
import com.varpihovsky.jetiq.back.api.managers.JetIQProfileManager
import com.varpihovsky.jetiq.back.api.managers.JetIQSubjectManager
import com.varpihovsky.jetiq.system.ConnectionManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ServerApiModule {
    @Provides
    @Singleton
    fun provideJetIQMessageManager(jetIQApi: JetIQApi, connectionManager: ConnectionManager) =
        JetIQMessageManager(jetIQApi, connectionManager)

    @Provides
    @Singleton
    fun provideJetIQSubjectManager(jetIQApi: JetIQApi, connectionManager: ConnectionManager) =
        JetIQSubjectManager(jetIQApi, connectionManager)

    @Provides
    @Singleton
    fun provideJetIQProfileManager(jetIQApi: JetIQApi, connectionManager: ConnectionManager) =
        JetIQProfileManager(jetIQApi, connectionManager)

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