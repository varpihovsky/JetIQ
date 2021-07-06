package com.varpihovsky.jetiq.back.dto

import com.varpihovsky.jetiq.ui.dto.ReceiverType

data class MessageToSendDTO(val id: Int, val type: ReceiverType, val body: String) {
    companion object {
        const val TYPE_STUDENT = "student"
        const val TYPE_TEACHER = "teacher"
    }
}