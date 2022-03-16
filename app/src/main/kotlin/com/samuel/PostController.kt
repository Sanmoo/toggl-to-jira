package com.samuel

import com.samuel.core.use_cases.GetTogglSummaryReportForPastWeek
import com.samuel.core.use_cases.MapTogglTimeRecordsToJIRAWorklogEntries
import com.samuel.core.use_cases.PostWorklogEntriesToJira

class PostController(
    private val getPastWeek: GetTogglSummaryReportForPastWeek,
    private val mapToJira: MapTogglTimeRecordsToJIRAWorklogEntries,
    private val postToJira: PostWorklogEntriesToJira
) {
    fun post(weeksOffsetFromToday: Int) {
        val get = getPastWeek.get(GetTogglSummaryReportForPastWeek.Input(weeksOffsetFromToday))

        val mapped = mapToJira.map(input = get.data.values.flatMap { it.list }.map {
            MapTogglTimeRecordsToJIRAWorklogEntries.TogglTimeRecordInput(
                title = it.title,
                timeInSeconds = it.timeInSeconds,
                start = it.start
            )
        })

        postToJira.post(input = mapped)
    }
}