package com.varpihovsky.core.di

import androidx.compose.runtime.Composable
import com.varpihovsky.core.lifecycle.ViewModel
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.qualifier.Qualifier
import org.koin.androidx.compose.getViewModel as vm

@Composable
actual inline fun <reified T : ViewModel> getViewModel(
    qualifier: Qualifier?,
    noinline parameters: ParametersDefinition?,
): T = vm(qualifier, parameters)
