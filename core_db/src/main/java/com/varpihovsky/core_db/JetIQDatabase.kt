package com.varpihovsky.core_db

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
    version = 9
)
abstract class JetIQDatabase : RoomDatabase() {
    abstract fun profileDAO(): ProfileDAO
    abstract fun confidentialDAO(): ConfidentialDAO
    abstract fun subjectDAO(): SubjectDAO
    abstract fun subjectDetailsDAO(): SubjectDetailsDAO
    abstract fun messageDAO(): MessageDAO
    abstract fun contactDAO(): ContactDAO
}