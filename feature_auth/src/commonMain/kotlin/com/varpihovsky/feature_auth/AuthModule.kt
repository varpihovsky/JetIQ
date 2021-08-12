package com.varpihovsky.feature_auth

import com.varpihovsky.core.di.viewModel
import com.varpihovsky.core.util.Validator
import org.koin.core.qualifier.qualifier
import org.koin.dsl.module

object AuthModule {
    val module = module {
        viewModel {
            AuthViewModel(
                get(),
                get(),
                get(qualifier = qualifier("login_checker")),
                get(qualifier = qualifier("password_checker")),
                get(),
                get(),
                get()
            )
        }

        factory(qualifier = qualifier("login_checker")) { provideLoginChecker() }
        factory(qualifier = qualifier("password_checker")) { providePasswordChecker() }
    }

    private fun provideLoginChecker(): Validator = object : Validator {
        override fun validate(t: String): Boolean =
            t.isNotEmpty()
    }

    private fun providePasswordChecker(): Validator = object : Validator {
        override fun validate(t: String): Boolean =
            t.isNotEmpty()
    }
}