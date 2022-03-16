package com.samuel.core.use_cases.ports

import com.samuel.core.entities.JiraWorklogEntry

interface JiraClient {
    fun isSimilarEntryAlreadyRegistered(entry: JiraWorklogEntry): Boolean
    fun logWork(entry: JiraWorklogEntry)
}