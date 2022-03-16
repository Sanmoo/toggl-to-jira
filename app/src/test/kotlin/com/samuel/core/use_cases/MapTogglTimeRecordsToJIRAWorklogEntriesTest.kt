package com.samuel.core.use_cases

import com.samuel.core.use_cases.MapTogglTimeRecordsToJIRAWorklogEntries.JiraWorklogEntryOutput
import com.samuel.core.use_cases.MapTogglTimeRecordsToJIRAWorklogEntries.TogglTimeRecordInput
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.time.ZoneId

internal class MapTogglTimeRecordsToJIRAWorklogEntriesTest {
    @Test
    fun `map from toggl to jira data structure`() {
        // Arrange
        val testingDate = LocalDateTime.of(2022, 3, 13, 21, 40, 0).atZone(ZoneId.systemDefault())
        val input = listOf(
            TogglTimeRecordInput("Soy un teste", 5, start = testingDate),
            TogglTimeRecordInput("PROJECT-2020: Soy un teste 2", 5, start = testingDate),
            TogglTimeRecordInput("PROJECT-2021 Soy un teste 3", 5, start = testingDate),
            TogglTimeRecordInput("PROJECT-2022", 5, start = testingDate),
        )

        // Act
        val output = MapTogglTimeRecordsToJIRAWorklogEntries().map(input)

        // Assert
        assertEquals(listOf(
            JiraWorklogEntryOutput(issueKey = "PROJECT-2020", amountInSeconds = 5, description = "Soy un teste 2", start = testingDate),
            JiraWorklogEntryOutput(issueKey = "PROJECT-2021", amountInSeconds = 5, description = "Soy un teste 3", start = testingDate),
            JiraWorklogEntryOutput(issueKey = "PROJECT-2022", amountInSeconds = 5, description = null, start = testingDate),
        ), output)
    }
}