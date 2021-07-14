package com.varpihovsky.core_nav

import com.varpihovsky.core_nav.main.NavigationControllerStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NavigationModule {
    @Provides
    fun provideNavigationManager(navigationControllerStorage: NavigationControllerStorage) =
        checkNotNull(navigationControllerStorage.navigationController)

    @Provides
    @Singleton
    fun provideStorage() = NavigationControllerStorage()
}