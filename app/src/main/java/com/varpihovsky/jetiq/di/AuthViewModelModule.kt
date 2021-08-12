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
package com.varpihovsky.jetiq.di

import com.varpihovsky.core.util.CoroutineDispatchers
import com.varpihovsky.core.util.Validator
import com.varpihovsky.jetiq.services.NotificationWorker
import com.varpihovsky.jetiq.services.SessionRestorationWorker
import kotlinx.coroutines.Dispatchers
import org.koin.androidx.workmanager.dsl.worker
import org.koin.core.qualifier.named
import org.koin.dsl.module

object ApplicationModule {
    val module = module {
        factory(qualifier = named("login_checker")) { provideLoginChecker() }
        factory(qualifier = named("password_checker")) { providePasswordChecker() }
        factory {
            CoroutineDispatchers(Dispatchers.IO, Dispatchers.Default, Dispatchers.Unconfined)
        }

        worker { NotificationWorker(get(), get(), get()) }
        worker { SessionRestorationWorker(get(), get(), get()) }
    }

    private fun provideLoginChecker() = object : Validator<String> {
        override fun validate(t: String): Boolean =
            t.isNotEmpty()
    }

    private fun providePasswordChecker() = object : Validator<String> {
        override fun validate(t: String): Boolean =
            t.isNotEmpty()
    }
}