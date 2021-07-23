package com.varpihovsky.core_db

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

import androidx.room.Database
import androidx.room.RoomDatabase
import com.varpihovsky.core_db.dao.*
import com.varpihovsky.repo_data.*

@Database(
    entities = [
        ProfileDTO::class,
        Confidential::class,
        SubjectDTO::class,
        SubjectDetailsDTO::class,
        SubjectTaskDTO::class,
        MarkbookSubjectDTO::class,
        MessageDTO::class,
        ContactDTO::class
    ],
    version = 11
)
abstract class JetIQDatabase : RoomDatabase() {
    abstract fun profileDAO(): ProfileDAO
    abstract fun confidentialDAO(): ConfidentialDAO
    abstract fun subjectDAO(): SubjectDAO
    abstract fun subjectDetailsDAO(): SubjectDetailsDAO
    abstract fun messageDAO(): MessageDAO
    abstract fun contactDAO(): ContactDAO
}