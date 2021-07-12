package com.varpihovsky.repo_data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Confidential(@PrimaryKey(autoGenerate = false) val login: String, val password: String)