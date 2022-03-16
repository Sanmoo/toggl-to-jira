package com.samuel.core.use_cases

import com.samuel.core.entities.JiraWorklogEntry
import com.samuel.core.use_cases.MapTogglTimeRecordsToJIRAWorklogEntries.JiraWorklogEntryOutput
import com.samuel.core.use_cases.ports.JiraClient

class PostWorklogEntriesToJira(
    private val jiraClient: JiraClient
) {
    fun post(input: List<JiraWorklogEntryOutput>) {
        input.forEach {
            val entry = JiraWorklogEntry(
                issueKey = it.issueKey,
                amountInSeconds = it.amountInSeconds,
                description = it.description,
                start = it.start
            )

            if (jiraClient.isSimilarEntryAlreadyRegistered(entry)) {
                println("Skipping registry of similar entry in JIRA...")
                return@forEach
            }

            jiraClient.logWork(entry)
        }
    }
}