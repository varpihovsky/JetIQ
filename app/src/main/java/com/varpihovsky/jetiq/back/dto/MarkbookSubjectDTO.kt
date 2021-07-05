package com.varpihovsky.jetiq.back.dto

data class MarkbookSubjectDTO(
    val credits: String,
    val date: String,
    val ects: String,
    val form: String,
    val hours: String,
    val mark: String,
    val subj_name: String,
    val teacher: String,
    val total: Int
)