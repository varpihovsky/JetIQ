package com.varpihovsky.core.di

import androidx.compose.runtime.Composable
import com.varpihovsky.core.lifecycle.ViewModel
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.qualifier.Qualifier
import org.koin.mp.KoinPlatformTools

@Composable
actual inline fun <reified T : ViewModel> getViewModelActual(
    qualifier: Qualifier?,
    noinline parameters: ParametersDefinition?,
): T = KoinPlatformTools.defaultContext().get().get(qualifier, parameters)