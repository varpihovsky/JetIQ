package com.varpihovsky.feature_messages

import com.varpihovsky.core.di.viewModel
import org.koin.dsl.module

object MessagesModule {
    val module = module {
        viewModel { MessagesViewModel(get(), get(), get(), get()) }
    }
}