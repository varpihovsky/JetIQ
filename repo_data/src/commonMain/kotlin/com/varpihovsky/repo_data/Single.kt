package com.varpihovsky.repo_data

import org.kodein.db.model.orm.Metadata

interface Single : Metadata {
    override val id: Any
        get() = identifier

    companion object {
        const val identifier = 0
    }
}