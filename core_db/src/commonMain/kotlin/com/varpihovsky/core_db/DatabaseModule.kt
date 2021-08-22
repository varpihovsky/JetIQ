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
package com.varpihovsky.core_db

import com.varpihovsky.core.log.d
import com.varpihovsky.core_db.dao.*
import com.varpihovsky.core_db.internal.types.*
import com.varpihovsky.repo_data.Confidential
import com.varpihovsky.repo_data.ContactDTO
import com.varpihovsky.repo_data.ReadMessage
import com.varpihovsky.repo_data.UserPreferences
import org.kodein.db.DB
import org.kodein.db.TypeTable
import org.kodein.db.impl.inDir
import org.kodein.db.orm.kotlinx.KotlinxSerializer
import org.koin.core.module.Module
import org.koin.core.qualifier.qualifier
import org.koin.dsl.module

object DatabaseModule {
    internal const val PATH = "jetiq_db"

    val module = module {
        factory { SubjectDetailsDAO(get()) }
        factory { SubjectDAO(get()) }
        factory { ProfileDAO(get()) }
        factory { MessageDAO(get()) }
        factory { ContactDAO(get()) }
        factory { ConfidentialDAO(get()) }
        factory {
            PreferencesDAO(get()).apply {
                d("Created preferences")
            }
        }

        single {
            DB.inDir(get(qualifier = qualifier(PATH)))
                .open(
                    path = PATH,
                    KotlinxSerializer {
                        +Confidential.serializer()
                        +ContactDTO.serializer()
                        +ProfileInternal.serializer()
                        +MessageInternal.serializer()
                        +SubjectInternal.serializer()
                        +SubjectDetailsInternal.serializer()
                        +MarkbookSubjectInternal.serializer()
                        +UserPreferences.serializer()
                        +ReadMessage.serializer()
                        +SentMessageInternal.serializer()
                    },
                    TypeTable {
                        root<Confidential>()
                        root<ContactDTO>()
                        root<ProfileInternal>()
                        root<MessageInternal>()
                        root<SubjectInternal>()
                        root<MarkbookSubjectInternal>()
                        root<UserPreferences>()
                        root<ReadMessage>()
                        root<SentMessageInternal>()
                    }
                ).apply { d("Created db") }

        }

        providePath()
    }
}

internal expect fun Module.providePath()