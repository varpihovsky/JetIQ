package com.varpihovsky.core_network

import com.varpihovsky.core.ConnectionManager
import com.varpihovsky.core_network.managers.JetIQListManager
import com.varpihovsky.core_network.managers.JetIQMessageManager
import com.varpihovsky.core_network.managers.JetIQProfileManager
import com.varpihovsky.core_network.managers.JetIQSubjectManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideJetIQListManager(jetIQApi: JetIQApi, connectionManager: ConnectionManager) =
        JetIQListManager(jetIQApi, connectionManager)

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
    fun provideRetrofit(): Retrofit = Retrofit
        .Builder()
        .baseUrl("https://iq.vntu.edu.ua/b04213/curriculum/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}