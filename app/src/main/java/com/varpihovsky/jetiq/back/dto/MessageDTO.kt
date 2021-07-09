package com.varpihovsky.jetiq.back.dto

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.varpihovsky.jetiq.ui.dto.UIMessageDTO
import org.threeten.bp.Instant
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId
import org.threeten.bp.format.DateTimeFormatter

@Entity
data class MessageDTO(
    val body: String?,
    val id_from: String,
    val is_t_from: String,
    @PrimaryKey(autoGenerate = false) val msg_id: String,
    val time: String
) {
    fun toUIDTO(): UIMessageDTO {
        val split = body!!.split("<b>", "</b>:<br>")
        return UIMessageDTO(
            msg_id.toInt(),
            split[1],
            split[2],
            Instant.ofEpochSecond(time.toLong()).let {
                DateTimeFormatter.ofPattern("HH:mm:ss dd.MM.yyyy")
                    .format(LocalDateTime.ofInstant(it, ZoneId.systemDefault()))
            }
        )
    }
}