package com.varpihovsky.core_network

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

import com.varpihovsky.core_network.managers.JetIQListManager
import com.varpihovsky.core_network.managers.JetIQMessageManager
import com.varpihovsky.core_network.managers.JetIQProfileManager
import com.varpihovsky.core_network.managers.JetIQSubjectManager
import com.varpihovsky.core_network.result.adapter.ResultAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideJetIQListManager(jetIQApi: JetIQApi) =
        JetIQListManager(jetIQApi)

    @Provides
    @Singleton
    fun provideJetIQMessageManager(jetIQApi: JetIQApi) =
        JetIQMessageManager(jetIQApi)

    @Provides
    @Singleton
    fun provideJetIQSubjectManager(jetIQApi: JetIQApi) =
        JetIQSubjectManager(jetIQApi)

    @Provides
    @Singleton
    fun provideJetIQProfileManager(jetIQApi: JetIQApi) =
        JetIQProfileManager(jetIQApi)

    @Provides
    @Singleton
    fun provideJetIQApi(retrofit: Retrofit) = retrofit.create(JetIQApi::class.java) as JetIQApi

    @Provides
    @Singleton
    fun provideRetrofit(@Named("Retrofit") client: OkHttpClient): Retrofit {
        return Retrofit
            .Builder()
            .client(client)
            .baseUrl("https://iq.vntu.edu.ua/b04213/curriculum/")
            .addCallAdapterFactory(ResultAdapterFactory())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    @Named("Retrofit")
    fun provideOkHttpClient() =
        OkHttpClient.Builder().addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }).build()
}