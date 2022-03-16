package com.samuel.core.entities

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

internal class UtilsTest {
    @ParameterizedTest
    @CsvSource("3600,1h", "3659,1h 59s", "3660,1h 1m", "28800,8h", "30180,8h 23m", "30200,8h 23m 20s")
    fun `convert time spent to string representation correctly`(amountInSeconds: Int, representation: String) {
        assertEquals(representation, timeSpentAsString(amountInSeconds))
    }
}