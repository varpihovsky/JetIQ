package com.varpihovsky.core_db.internal.types

import com.varpihovsky.repo_data.Listable
import kotlinx.serialization.Serializable

@Serializable
internal class SentMessageInternal(
    override val id: Int,
    val receiverId: Int,
    val type: Int,
    val body: String,
    val time: Long
) :
    Listable {
    override fun with(id: Int): Listable = SentMessageInternal(id, receiverId, type, body, time)
}