package com.samuel.core.entities

import java.time.ZonedDateTime

data class WorklogSummary(val title: String, val timeInSeconds: Long, val start: ZonedDateTime)