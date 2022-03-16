package com.samuel.adapters

import com.beust.klaxon.JsonArray
import com.beust.klaxon.JsonObject
import com.beust.klaxon.Klaxon
import com.samuel.core.entities.JiraWorklogEntry
import com.samuel.core.use_cases.ports.JiraClient
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import java.io.StringReader
import java.time.Duration
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.abs

class JiraRestApiClient(
    private val client: HttpClient,
    private val apiToken: String,
    private val username: String,
    private val jiraBaseUrl: String
) : JiraClient {
    override fun isSimilarEntryAlreadyRegistered(entry: JiraWorklogEntry): Boolean {
        return runBlocking {
            val response =
                client.request("${jiraBaseUrl}/rest/api/3/issue/${entry.issueKey}/worklog") {

                    basicAuth(username = username, password = apiToken)
                }

            val bodyAsText = response.bodyAsText()
            val jsonResponse = Klaxon().parseJsonObject(StringReader(bodyAsText))

            val dataArray = jsonResponse.array<JsonObject>("worklogs")!!

            if (dataArray.isEmpty()) {
                return@runBlocking false
            }

            val entriesAlreadyRegistered = dataArray.map {
                JiraWorklogEntry(
                    entry.issueKey,
                    it.int("timeSpentSeconds")!!,
                    description = it.obj("comment")?.array<JsonObject>("content")?.get(0)?.array<JsonObject>("content")?.get(0)?.string("text"),
                    start = ZonedDateTime.parse(it.string("started"), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS[xxx][xx][X]"))
                )
            }

            val find = entriesAlreadyRegistered.find { it.isSimilarTo(entry) }
            if (find != null) {
                println("Found entry in JIRA similar to one trying to register. Trying to enter: '${entry.toSimpleString()}'. One found: ${find.toSimpleString()}")
                return@runBlocking true
            }

            return@runBlocking false
        }
    }

    override fun logWork(entry: JiraWorklogEntry) {
        println("Sending to JIRA: ${entry.toSimpleString()}")
        val body = JsonObject(
            mapOf(
                "timeSpentSeconds" to entry.amountInSeconds,
                "started" to entry.start.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ")),
                "comment" to JsonObject(
                    mapOf(
                        "type" to "doc",
                        "version" to 1,
                        "content" to JsonArray(
                            listOf(
                                JsonObject(
                                    mapOf(
                                        "type" to "paragraph",
                                        "content" to JsonArray(
                                            listOf(
                                                JsonObject(
                                                    mapOf(
                                                        "text" to entry.description,
                                                        "type" to "text"
                                                    )
                                                )
                                            )
                                        )
                                    )
                                )
                            )
                        )
                    )
                )
            )
        ).toJsonString()
        println("Json: $body")

        runBlocking {
            client.post("${jiraBaseUrl}/rest/api/3/issue/${entry.issueKey}/worklog") {
                basicAuth(username = username, password = apiToken)
                method = HttpMethod.Post
                setBody(body)
                headers { header("Content-Type", "application/json") }
            }
        }
    }
}

private fun JiraWorklogEntry.isSimilarTo(entry: JiraWorklogEntry): Boolean {
    // JIRA only stores durations up to the precision of minutes
    val isDurationSimilar = abs(entry.amountInSeconds - this.amountInSeconds) < 120
    val isForSameDate = entry.start.dayOfYear == this.start.dayOfYear && entry.start.year == this.start.year
    return isDurationSimilar && isForSameDate && Duration.between(
        entry.start,
        this.start
    ).abs().seconds < 15
}
