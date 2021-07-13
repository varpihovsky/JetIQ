package com.varpihovsky.ui_data.mappers

import com.varpihovsky.repo_data.*
import com.varpihovsky.ui_data.*
import org.threeten.bp.Instant
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId
import org.threeten.bp.format.DateTimeFormatter


fun ContactDTO.toUIDTO() = UIReceiverDTO(
    id,
    text,
    when (type) {
        ContactDTO.TYPE_STUDENT -> ReceiverType.STUDENT
        ContactDTO.TYPE_TEACHER -> ReceiverType.TEACHER
        else -> throw RuntimeException()
    }
)

fun MarkbookSubjectDTO.toUIDTO() = UISubjectDTO(
    id,
    subj_name,
    teacher,
    total,
    semester
)

fun MessageDTO.toUIDTO(): UIMessageDTO {
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

fun ProfileDTO.toUIDTO() = UIProfileDTO(
    id.toInt(),
    getUsername(u_name),
    cutFacultyName(d_name),
    course_num,
    gr_name,
    0,
    photo_url
)

private fun getUsername(name: String): String {
    val strings = name.split(" ").subList(0, 2)
    return "${strings[0]} ${strings[1]}"
}

private fun cutFacultyName(faculty: String) =
    String(faculty.split(" ").filter { it.length > 2 }.map { it.first().uppercaseChar() }
        .toCharArray())

fun SubjectDTO.toUIDTO(total: Int) = UISubjectDTO(
    card_id.toInt(),
    subject,
    t_name,
    total,
    sem.toInt()
)