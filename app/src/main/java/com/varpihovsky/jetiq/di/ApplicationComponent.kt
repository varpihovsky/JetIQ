package com.varpihovsky.jetiq.di

import com.varpihovsky.jetiq.appbar.AppbarManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApplicationComponent {
    @Provides
    @Singleton
    fun provideAppbarManager() = AppbarManager()
}