package com.varpihovsky.core.di

import androidx.compose.runtime.Composable
import com.varpihovsky.core.lifecycle.ViewModel
import org.koin.core.context.GlobalContext
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.qualifier.Qualifier

@Composable
actual inline fun <reified T : ViewModel> getViewModelActual(
    qualifier: Qualifier?,
    noinline parameters: ParametersDefinition?,
): T = GlobalContext.get().get(qualifier, parameters)