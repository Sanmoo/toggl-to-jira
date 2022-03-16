package com.samuel.core.use_cases

import com.samuel.core.entities.WorklogSummary
import com.samuel.core.use_cases.ports.TogglClient
import java.time.ZonedDateTime

class GetTogglSummaryReport(private val togglClient: TogglClient) {
    data class Output(val list: List<WorklogSummaryOutput>)
    data class WorklogSummaryOutput(val title: String, val timeInSeconds: Long, val start: ZonedDateTime) {
        companion object {
            fun fromEntity(l: WorklogSummary): WorklogSummaryOutput {
                return WorklogSummaryOutput(title = l.title, timeInSeconds = l.timeInSeconds, start = l.start)
            }
        }
    }

    fun get(day: ZonedDateTime): Output {
        val worklogSummariesByDate = togglClient.getWorklogSummariesByDate(day)
        return Output(list = worklogSummariesByDate.map { WorklogSummaryOutput.fromEntity(it) })
    }
}