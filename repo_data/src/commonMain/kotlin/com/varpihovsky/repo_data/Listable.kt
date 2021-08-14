package com.varpihovsky.repo_data

import org.kodein.db.model.orm.Metadata

interface Listable<T : Metadata> : Metadata {
    override val id: Int

    fun with(id: Int): Listable<T>
}