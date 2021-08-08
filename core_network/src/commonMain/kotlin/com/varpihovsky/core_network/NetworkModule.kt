package com.varpihovsky.core_network

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

import com.varpihovsky.core_network.managers.JetIQListManager
import com.varpihovsky.core_network.managers.JetIQMessageManager
import com.varpihovsky.core_network.managers.JetIQProfileManager
import com.varpihovsky.core_network.managers.JetIQSubjectManager
import org.koin.core.module.Module
import org.koin.dsl.module

object NetworkModule {
    val module = module {
        factory { JetIQListManager(get()) }
        factory { JetIQMessageManager(get()) }
        factory { JetIQSubjectManager(get()) }
        factory { JetIQProfileManager(get()) }

        provideApi()
    }
}

internal expect fun Module.provideApi()