package com.varpihovsky.feature_auth

import com.varpihovsky.core.util.Validator
import org.koin.core.qualifier.qualifier
import org.koin.dsl.module

object AuthModule {
    val module = module {
        factory(qualifier = qualifier("login_checker")) { provideLoginChecker() }
        factory(qualifier = qualifier("password_checker")) { providePasswordChecker() }
    }

    private fun provideLoginChecker(): Validator = Validator { it.isNotEmpty() }

    private fun providePasswordChecker(): Validator = Validator { it.isNotEmpty() }
}