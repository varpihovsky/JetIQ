package com.varpihovsky.jetiq.di

import com.varpihovsky.jetiq.system.util.Checker
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import javax.inject.Named

@Module
@InstallIn(ViewModelComponent::class)
class AuthViewModelModule {
    @Named("login_checker")
    @Provides
    fun provideLoginChecker() = object : Checker<String> {
        override fun check(t: String): Boolean =
            t.isNotEmpty()
    }

    @Named("password_checker")
    @Provides
    fun providePasswordChecker() = object : Checker<String> {
        override fun check(t: String): Boolean =
            t.isNotEmpty()
    }
}