package com.varpihovsky.jetiq.di

import com.varpihovsky.core.util.Validator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import javax.inject.Named

@Module
@InstallIn(ViewModelComponent::class)
object AuthViewModelModule {
    @Named("login_checker")
    @Provides
    fun provideLoginChecker() = object : Validator<String> {
        override fun validate(t: String): Boolean =
            t.isNotEmpty()
    }

    @Named("password_checker")
    @Provides
    fun providePasswordChecker() = object : Validator<String> {
        override fun validate(t: String): Boolean =
            t.isNotEmpty()
    }
}