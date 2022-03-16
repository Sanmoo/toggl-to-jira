package com.samuel.core.use_cases

import com.samuel.core.entities.timeSpentAsString
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class MapTogglTimeRecordsToJIRAWorklogEntries {
    data class TogglTimeRecordInput(val title: String, val timeInSeconds: Long, val start: ZonedDateTime)
    data class JiraWorklogEntryOutput(
        val issueKey: String,
        val amountInSeconds: Int,
        val description: String?,
        val start: ZonedDateTime
    )

    fun map(input: List<TogglTimeRecordInput>): List<JiraWorklogEntryOutput> {
        val output = mutableListOf<JiraWorklogEntryOutput>()
        input.forEach { item ->
            val regex = "([A-Z]+-\\d+):?\\s*(.*)".toRegex(RegexOption.IGNORE_CASE)
            val matchResult = regex.matchEntire(item.title)
            if (matchResult == null) {
                println(
                    "Log entry \"${item.title}\", which took ${
                        timeSpentAsString(item.timeInSeconds.toInt())
                    } and started at ${item.start.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"))}, is not properly formatted to be sent to JIRA. So skipping..."
                )
                return@forEach
            }
            val groupValues = matchResult.groupValues
            val description = (if (groupValues.size > 2) groupValues[2] else null)?.let { it.ifEmpty { null } }
            output.add(
                JiraWorklogEntryOutput(
                    groupValues[1],
                    item.timeInSeconds.toInt(),
                    description,
                    item.start
                )
            )
        }

        return output
    }
}