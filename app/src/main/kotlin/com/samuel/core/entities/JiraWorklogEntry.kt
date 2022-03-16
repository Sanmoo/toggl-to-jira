package com.samuel.core.entities

import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

data class JiraWorklogEntry(val issueKey: String, val amountInSeconds: Int, val description: String?, val start: ZonedDateTime) {
    fun toSimpleString(): String {
        return "$issueKey: $description (${timeSpentAsString(amountInSeconds)}) - ${start.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ssZ"))}"
    }
}