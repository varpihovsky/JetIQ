package com.varpihovsky.core_repo

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

import com.varpihovsky.core_repo.repo.ListRepo
import com.varpihovsky.core_repo.repo.MessagesRepo
import com.varpihovsky.core_repo.repo.ProfileRepo
import com.varpihovsky.core_repo.repo.SubjectRepo
import com.varpihovsky.jetiqApi.provideApi
import org.koin.core.module.Module
import org.koin.dsl.module

object RepoModule {

    val module = module {
        factory { MessagesRepo(get(), get(), get(), get(), get()) }
        factory { ProfileRepo(get(), get(), get(), get()) }
        factory { ListRepo(get(), get(), get()) }
        factory { SubjectRepo(get(), get(), get(), get(), get(), get(), get()) }

        single { provideApi {} }

        providePlatform()
    }
}

internal expect fun Module.providePlatform()