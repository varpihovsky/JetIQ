package com.varpihovsky.core.di

import com.varpihovsky.core.ConnectionManager
import com.varpihovsky.core.dataTransfer.ViewModelDataTransferManager
import com.varpihovsky.core.navigation.NavigationManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CoreModule {
    @Provides
    fun provideConnectionManager() = ConnectionManager()

    @Provides
    @Singleton
    fun provideDataTransferManager() = ViewModelDataTransferManager()

    @Provides
    @Singleton
    fun provideNavigationManager() = NavigationManager()
}