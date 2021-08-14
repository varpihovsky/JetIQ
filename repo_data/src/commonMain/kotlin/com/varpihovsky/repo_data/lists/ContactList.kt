package com.varpihovsky.repo_data.lists

import com.varpihovsky.repo_data.ContactDTO
import com.varpihovsky.repo_data.SingleHolder
import kotlinx.serialization.Serializable
import org.kodein.db.Key

@Serializable
class ContactList(override val list: List<Key<ContactDTO>>) : SingleHolder<ContactDTO> {
    override fun with(list: List<Key<ContactDTO>>) = ContactList(list)
}