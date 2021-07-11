package com.varpihovsky.jetiq.back.db.managers

import com.varpihovsky.jetiq.back.db.dao.ContactDAO
import com.varpihovsky.jetiq.back.dto.ContactDTO
import javax.inject.Inject

class ContactDatabaseManager @Inject constructor(
    private val contactDAO: ContactDAO
) {
    fun getContacts() = contactDAO.getContacts()

    fun addContact(contactDTO: ContactDTO) {
        contactDAO.insert(contactDTO)
    }

    fun removeContact(contactDTO: ContactDTO) {
        contactDAO.delete(contactDTO)
    }

    fun clear() {
        contactDAO.clear()
    }
}