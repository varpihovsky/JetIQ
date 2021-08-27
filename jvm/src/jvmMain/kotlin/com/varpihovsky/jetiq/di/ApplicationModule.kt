package com.varpihovsky.jetiq.di

import com.varpihovsky.core.util.CoroutineDispatchers
import kotlinx.coroutines.Dispatchers
import org.koin.dsl.module

object ApplicationModule {
    val module = module {
        factory { CoroutineDispatchers(Dispatchers.IO, Dispatchers.Default, Dispatchers.Unconfined) }
    }
}