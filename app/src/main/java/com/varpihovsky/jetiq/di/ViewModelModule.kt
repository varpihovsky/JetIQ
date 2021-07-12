package com.varpihovsky.jetiq.di

import com.varpihovsky.core.util.CoroutineDispatchers
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import kotlinx.coroutines.Dispatchers

@Module
@InstallIn(ViewModelComponent::class)
object ViewModelModule {
    @Provides
    fun provideDispatchers() = CoroutineDispatchers(Dispatchers.IO)
}