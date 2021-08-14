package com.varpihovsky.repo_data

import org.kodein.db.Key
import org.kodein.db.model.orm.Metadata

interface SingleHolder<T : Metadata> : Single {
    val list: List<Key<T>>

    fun with(list: List<Key<T>>): SingleHolder<T>
}