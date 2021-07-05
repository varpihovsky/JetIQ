package com.varpihovsky.jetiq.back.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.varpihovsky.jetiq.back.db.dao.ConfidentialDAO
import com.varpihovsky.jetiq.back.db.dao.ProfileDAO
import com.varpihovsky.jetiq.back.db.dao.SubjectDAO
import com.varpihovsky.jetiq.back.db.dao.SubjectDetailsDAO
import com.varpihovsky.jetiq.back.dto.*

@Database(
    entities = [
        ProfileDTO::class,
        Confidential::class,
        SubjectDTO::class,
        SubjectDetailsDTO::class,
        SubjectTaskDTO::class,
        MarkbookSubjectDTO::class
    ],
    version = 6
)
abstract class JetIQDatabase : RoomDatabase() {
    abstract fun profileDAO(): ProfileDAO
    abstract fun confidentialDAO(): ConfidentialDAO
    abstract fun subjectDAO(): SubjectDAO
    abstract fun subjectDetailsDAO(): SubjectDetailsDAO
}