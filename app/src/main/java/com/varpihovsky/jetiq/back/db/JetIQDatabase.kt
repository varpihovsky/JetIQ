package com.varpihovsky.jetiq.back.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.varpihovsky.jetiq.back.db.dao.*
import com.varpihovsky.jetiq.back.dto.*

@Database(
    entities = [
        ProfileDTO::class,
        Confidential::class,
        SubjectDTO::class,
        SubjectDetailsDTO::class,
        SubjectTaskDTO::class,
        MarkbookSubjectDTO::class,
        MessageDTO::class
    ],
    version = 7
)
abstract class JetIQDatabase : RoomDatabase() {
    abstract fun profileDAO(): ProfileDAO
    abstract fun confidentialDAO(): ConfidentialDAO
    abstract fun subjectDAO(): SubjectDAO
    abstract fun subjectDetailsDAO(): SubjectDetailsDAO
    abstract fun messageDAO(): MessageDAO
}