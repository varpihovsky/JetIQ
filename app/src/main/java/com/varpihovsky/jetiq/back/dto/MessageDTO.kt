package com.varpihovsky.jetiq.back.dto

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MessageDTO(
    val body: String?,
    val id_from: String,
    val is_t_from: String,
    @PrimaryKey(autoGenerate = false) val msg_id: String,
    val time: String
)