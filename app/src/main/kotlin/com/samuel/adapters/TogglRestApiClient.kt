package com.samuel.adapters

import com.beust.klaxon.JsonObject
import com.beust.klaxon.Klaxon
import com.samuel.core.entities.WorklogSummary
import com.samuel.core.use_cases.ports.TogglClient
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.runBlocking
import java.io.StringReader
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoField

class TogglRestApiClient(
    private val client: HttpClient,
    private val apiToken: String,
    private val userAgent: String,
    private val projectIds: List<Long>,
    private val workspaceId: List<Long>,
) : TogglClient {
    override fun getWorklogSummariesByDate(date: ZonedDateTime): List<WorklogSummary> {
        return runBlocking {
            val response = client.request("https://api.track.toggl.com/reports/api/v2/summary") {
                parameter("user_agent", userAgent)
                parameter("project_ids", projectIds.joinToString(","))
                parameter(
                    "since", date.with(ChronoField.MINUTE_OF_DAY, 0).format(
                        DateTimeFormatter.ISO_OFFSET_DATE_TIME
                    )
                )
                parameter(
                    "until", date.with(ChronoField.HOUR_OF_DAY, 23).with(ChronoField.MINUTE_OF_HOUR, 59).format(
                        DateTimeFormatter.ISO_OFFSET_DATE_TIME
                    )
                )
                parameter("workspace_id", workspaceId.joinToString(","))
                basicAuth(apiToken, "api_token")
            }
            println("Requesting toggl info ${response.request.url.encodedQuery}")

            val responseStr = response.bodyAsText()
            val jsonResponse = Klaxon().parseJsonObject(StringReader(responseStr))

            val dataArray = jsonResponse.array<JsonObject>("data")!!

            if (dataArray.isEmpty()) {
                return@runBlocking listOf()
            }

            val items = dataArray[0].array<JsonObject>("items")!!
            return@runBlocking items.map {
                WorklogSummary(
                    title = it.obj("title")?.string("time_entry")!!,
                    timeInSeconds = it.long("time")!! / 1000,
                    // Unfortunately I did not find a way for the API to return a zoned date time
                    start = LocalDateTime.parse(it.string("local_start"), DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                        .atZone(ZoneId.systemDefault())
                )
            }
        }
    }
}