package com.samuel.view

import com.samuel.core.entities.timeSpentAsString
import com.samuel.core.use_cases.GetTogglSummaryReportForPastWeek
import java.time.format.DateTimeFormatter

class ToggleSummaryReportForWeek(
    private val input: GetTogglSummaryReportForPastWeek.Output
) {
    fun render(): String {
        val blocks = input.data.map { (key, value) ->
            val formattedDay = key.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
            var output = ""
            output += formattedDay + "\n"
            output += formattedDay.replace(Regex("."), "-") + "\n"
            value.list.forEach {
                output += "\n${it.title} (${timeSpentAsString(it.timeInSeconds.toInt())})"
            }
            return@map output
        }

        return blocks.joinToString("\n\n")
    }
}