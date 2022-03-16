package com.samuel

import java.io.File
import java.io.FileInputStream
import java.util.*

class TogglToJiraProperties {
    lateinit var togglApiToken: String
    lateinit var togglUserAgent: String
    lateinit var togglProjectIds: List<Long>
    lateinit var togglWorkspaceId: List<Long>
    lateinit var jiraApiToken: String
    lateinit var jiraUsername: String
    lateinit var jiraBaseUrl: String

    companion object {
        fun loadFromFile(file: File): TogglToJiraProperties {
            val prop = Properties()
            FileInputStream(file).use { prop.load(it) }

            prop.stringPropertyNames()
                .associateWith { prop.getProperty(it) }

            val result = TogglToJiraProperties()

            result.togglApiToken = prop.getProperty("togglApiToken")
            result.togglUserAgent = prop.getProperty("togglUserAgent")
            result.togglProjectIds = prop.getProperty("togglProjectIds").split(",").map { it.toLong() }
            result.togglWorkspaceId = prop.getProperty("togglWorkspaceId").split(",").map { it.toLong() }
            result.jiraApiToken = prop.getProperty("jiraApiToken")
            result.jiraUsername = prop.getProperty("jiraUsername")
            result.jiraBaseUrl = prop.getProperty("jiraBaseUrl")

            return result
        }
    }
}