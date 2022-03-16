package com.samuel.core.use_cases.ports

import com.samuel.core.entities.WorklogSummary
import java.time.ZonedDateTime

interface TogglClient {
    fun getWorklogSummariesByDate(date: ZonedDateTime): List<WorklogSummary>
}