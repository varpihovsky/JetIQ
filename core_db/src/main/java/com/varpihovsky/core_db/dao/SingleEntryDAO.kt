package com.varpihovsky.core_db.dao

import kotlinx.coroutines.flow.Flow

interface SingleEntryDAO<T> {
    fun get(): Flow<T>
    fun set(t: T)
    fun delete()
}

fun <T> SingleEntryDAO<T>.reset(value: T) {
    delete()
    set(value)
}