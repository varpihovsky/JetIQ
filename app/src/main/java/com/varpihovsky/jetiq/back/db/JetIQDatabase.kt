package com.varpihovsky.jetiq.back.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.varpihovsky.jetiq.back.db.dao.ProfileDAO
import com.varpihovsky.jetiq.back.dto.Profile

@Database(
    entities = [Profile::class],
    version = 1
)
abstract class JetIQDatabase: RoomDatabase() {
    abstract fun profileDAO(): ProfileDAO
}