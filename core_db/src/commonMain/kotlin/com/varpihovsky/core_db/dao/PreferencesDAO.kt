package com.varpihovsky.core_db.dao

import com.varpihovsky.core_db.internal.delete
import com.varpihovsky.core_db.internal.keyById
import com.varpihovsky.core_db.internal.putSingle
import com.varpihovsky.repo_data.UserPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull
import org.kodein.db.DB
import org.kodein.db.flowOf

interface PreferencesDAO : SingleEntryDAO<UserPreferences> {
    companion object {
        operator fun invoke(db: DB): PreferencesDAO = PreferencesDAOImpl(db)
    }
}

private class PreferencesDAOImpl(private val db: DB) : PreferencesDAO {
    override fun get(): Flow<UserPreferences?> = db.flowOf(db.keyById()).mapNotNull {
        it?.let { UserPreferences() }
    }

    override fun set(t: UserPreferences) {
        db.putSingle(t)
    }

    override fun delete() {
        db.delete<UserPreferences>()
    }
}