package com.samuel.core.use_cases

import java.time.DayOfWeek
import java.time.ZonedDateTime

class GetTogglSummaryReportForPastWeek(
    private val getTogglSummaryReport: GetTogglSummaryReport
) {
    data class Input(val weeksOffsetFromToday: Int)
    data class Output(val data: Map<ZonedDateTime, GetTogglSummaryReport.Output>)

    fun get(input: Input): Output {
        val beginningOfWeek = ZonedDateTime.now().minusWeeks(input.weeksOffsetFromToday.toLong()).with(DayOfWeek.MONDAY)
        val resultMap = HashMap<ZonedDateTime, GetTogglSummaryReport.Output>()
        (0..6).forEach {
            val dateToQuery = beginningOfWeek.plusDays(it.toLong())
            resultMap[dateToQuery] = getTogglSummaryReport.get(dateToQuery)
        }

        return Output(data = resultMap.toSortedMap())
    }
}