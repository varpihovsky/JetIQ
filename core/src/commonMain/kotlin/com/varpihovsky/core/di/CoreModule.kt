package com.varpihovsky.core.di

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

import com.varpihovsky.core.appbar.AppbarManager
import com.varpihovsky.core.dataTransfer.ViewModelDataTransferManager
import com.varpihovsky.core.eventBus.EventBus
import com.varpihovsky.core.exceptions.ExceptionEventManager
import com.varpihovsky.core.lifecycle.SimpleStateHolder
import org.koin.dsl.module

object CoreModule {
    val module = module {
        factory { ExceptionEventManager(get()) }
        factory { AppbarManager(get()) }

        single { EventBus() }
        single { ViewModelDataTransferManager() }
        single { SimpleStateHolder() }
    }
}