package com.samuel.view

import au.com.origin.snapshots.Expect
import au.com.origin.snapshots.junit5.SnapshotExtension
import com.samuel.core.use_cases.GetTogglSummaryReport
import com.samuel.core.use_cases.GetTogglSummaryReportForPastWeek
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.time.ZoneId
import java.time.ZonedDateTime

@ExtendWith(SnapshotExtension::class)
internal class ToggleSummaryReportForWeekTest {
    private lateinit var expect: Expect

    @Test
    fun `render report correctly`() {
        val day1 = ZonedDateTime.of(2022, 3, 14, 8, 0, 0, 0, ZoneId.systemDefault())
        val day2 = day1.plusDays(1)
        val day3 = day2.plusDays(1)
        val sut = ToggleSummaryReportForWeek(
            input = GetTogglSummaryReportForPastWeek.Output(
                data = mapOf(
                    day1 to GetTogglSummaryReport.Output(
                        list = listOf(
                            GetTogglSummaryReport.WorklogSummaryOutput(title = "Soy un log amigo", timeInSeconds = 328489, start = day1),
                            GetTogglSummaryReport.WorklogSummaryOutput(title = "Soy un otro log", timeInSeconds = 3289, start = day1)
                        )
                    ),
                    day2 to GetTogglSummaryReport.Output(
                        list = listOf(
                            GetTogglSummaryReport.WorklogSummaryOutput(title = "JIRA-123: Done this and that", timeInSeconds = 28489, start = day2),
                            GetTogglSummaryReport.WorklogSummaryOutput(title = "Done this and that and that", timeInSeconds = 32890, start = day2)
                        )
                    ),
                    day3 to GetTogglSummaryReport.Output(
                        list = listOf(
                            GetTogglSummaryReport.WorklogSummaryOutput(title = "JIRA-123: Done this and that", timeInSeconds = 28489, start = day3),
                            GetTogglSummaryReport.WorklogSummaryOutput(title = "JIRA-432: Dooooooone this and that", timeInSeconds = 35890, start = day3),
                            GetTogglSummaryReport.WorklogSummaryOutput(title = "JIRA-1500: Dooooooone this and that", timeInSeconds = 65890, start = day3)
                        )
                    )
                )
            )
        )

        expect.toMatchSnapshot(sut.render())
    }
}