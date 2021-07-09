package com.varpihovsky.jetiq.di

import com.varpihovsky.jetiq.system.util.CoroutineDispatchers
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object ViewModelModule {
    @Provides
    fun provideDispatchers() = CoroutineDispatchers(kotlinx.coroutines.Dispatchers.IO)
}