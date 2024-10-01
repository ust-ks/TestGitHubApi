package ru.example.mobile.testgithubapi.presentation.utils

import java.time.LocalDateTime
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

fun LocalDateTime.toFormatDateTime(): String {
    val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm")
    return this.format(formatter)
}

fun String.toLocalDateTime() : LocalDateTime? {
    return try {
        val zonedDateTime = ZonedDateTime.parse(this)
        zonedDateTime.toLocalDateTime()
    } catch (e: Exception) {
        null
    }
}