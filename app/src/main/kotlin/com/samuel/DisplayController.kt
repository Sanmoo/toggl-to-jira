package com.samuel

import com.samuel.core.use_cases.GetTogglSummaryReportForPastWeek
import com.samuel.view.ToggleSummaryReportForWeek

class DisplayController (private val getPastWeek: GetTogglSummaryReportForPastWeek) {
    fun display(weeksOffsetFromToday: Int): String {
        val get = getPastWeek.get(GetTogglSummaryReportForPastWeek.Input(weeksOffsetFromToday))
        return "\n\n" + ToggleSummaryReportForWeek(get).render() + "\n"
    }
}