package com.harishkannarao.ktor.util

import java.time.OffsetDateTime
import java.time.ZoneOffset

object DateTimeUtil {
    fun toUtcZoneOffset(input: OffsetDateTime): OffsetDateTime {
        return input.withOffsetSameInstant(ZoneOffset.UTC)
    }
}