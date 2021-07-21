package com.varpihovsky.jetiq.di

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