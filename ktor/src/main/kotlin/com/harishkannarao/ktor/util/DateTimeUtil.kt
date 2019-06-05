package com.harishkannarao.ktor.util

import java.time.Instant
import java.time.LocalDate
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

object DateTimeUtil {
    fun toUtcZoneOffset(input: OffsetDateTime): OffsetDateTime {
        return input.withOffsetSameInstant(ZoneOffset.UTC)
    }

    fun toUtcOffsetDateTime(epochMillis: Long): OffsetDateTime {
        return Instant.ofEpochMilli(epochMillis).atOffset(ZoneOffset.UTC)
    }

    fun toIsoTimeStampString(offsetDateTime: OffsetDateTime): String {
        return offsetDateTime.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)
    }

    fun toIsoLocalDateString(input: LocalDate): String {
        return input.format(DateTimeFormatter.ISO_LOCAL_DATE)
    }
}