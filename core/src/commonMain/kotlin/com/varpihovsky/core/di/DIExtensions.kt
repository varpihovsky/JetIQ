/* JetIQ
 * Copyright © 2021 Vladyslav Podrezenko
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.varpihovsky.core.di

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.varpihovsky.core.lifecycle.ViewModel
import org.koin.core.definition.Definition
import org.koin.core.instance.InstanceFactory
import org.koin.core.module.Module
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.qualifier.Qualifier
import org.koin.mp.KoinPlatformTools


/**
 * Resolve dependency for composable function.
 */
@Composable
inline fun <reified T : Any> get(
    qualifier: Qualifier? = null,
    noinline parameters: ParametersDefinition? = null
): T = remember(qualifier, parameters) {
    // Due to koin supports injection in composable function only in android source sets
    // we need to copy paste that thing in common source set.
    // GlobalContext in koin sources and defaultContext are same.
    KoinPlatformTools.defaultContext().get().get()
}

inline fun <reified T : ViewModel> Module.viewModel(
    qualifier: Qualifier? = null,
    noinline definition: Definition<T>
): Pair<Module, InstanceFactory<T>> = factory(qualifier, definition)

@Composable
expect inline fun <reified T : ViewModel> getViewModel(
    qualifier: Qualifier? = null,
    noinline parameters: ParametersDefinition? = null,
): T
