package com.varpihovsky.jetiq.di

import com.varpihovsky.core.di.viewModel
import com.varpihovsky.core.util.CoroutineDispatchers
import com.varpihovsky.jetiq.NavigationViewModel
import kotlinx.coroutines.Dispatchers
import org.koin.dsl.module

object ApplicationModule {
    val module = module {
        viewModel { NavigationViewModel(get(), get()) }

        factory { CoroutineDispatchers(Dispatchers.IO, Dispatchers.Default, Dispatchers.Unconfined) }
    }
}