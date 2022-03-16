package com.samuel.core.entities

fun timeSpentAsString(amountInSeconds: Int): String {
    val amountInHours = amountInSeconds / (60 * 60)
    val restOfDivisionForDays = amountInSeconds % (60 * 60)
    val amountInMinutes = restOfDivisionForDays / 60
    val restOfDivisionForMinutes = restOfDivisionForDays % 60
    val resultParts = mutableListOf<String>()
    if (amountInHours > 0) {
        resultParts.add("${amountInHours}h")
    }

    if (amountInMinutes > 0) {
        resultParts.add("${amountInMinutes}m")
    }

    if (restOfDivisionForMinutes > 0) {
        resultParts.add("${restOfDivisionForMinutes}s")
    }

    return resultParts.joinToString(" ")
}