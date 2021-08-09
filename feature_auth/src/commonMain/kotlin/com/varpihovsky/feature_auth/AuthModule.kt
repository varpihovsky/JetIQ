package com.varpihovsky.feature_auth

import com.varpihovsky.core.di.viewModel
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
    }
}