package com.varpihovsky.jetiq.back.dto

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Confidential(@PrimaryKey(autoGenerate = false) val login: String, val password: String)