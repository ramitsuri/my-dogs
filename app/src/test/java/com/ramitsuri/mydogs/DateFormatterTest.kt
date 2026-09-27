package com.ramitsuri.mydogs

import org.junit.Assert.assertEquals
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class DateFormatterTest {

    @Test
    fun testUtcDateFormatting() {
        val formatter = SimpleDateFormat("MMM dd, yyyy", Locale.US).apply {
            timeZone = TimeZone.getTimeZone("UTC")
        }
        // January 1, 2024 00:00:00 UTC timestamp in millis
        // 2024-01-01T00:00:00.000Z = 1704067200000L
        val timestamp = 1704067200000L
        val formatted = formatter.format(Date(timestamp))
        assertEquals("Jan 01, 2024", formatted)
    }
}
