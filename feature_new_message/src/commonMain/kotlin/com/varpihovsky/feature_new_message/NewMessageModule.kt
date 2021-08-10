package com.varpihovsky.feature_new_message

import com.varpihovsky.core.di.viewModel
import org.koin.dsl.module

object NewMessageModule {
    val module = module {
        viewModel { NewMessageViewModel(get(), get(), get(), get(), get(), get()) }
    }
}