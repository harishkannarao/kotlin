package com.harishkannarao.ktor.util

import java.time.OffsetDateTime
import java.util.*

object TestDataUtil {
    fun randomString(): String {
        return UUID.randomUUID().toString()
    }

    fun currentUtcOffsetDateTime(): OffsetDateTime {
        return DateTimeUtil.toUtcZoneOffset(OffsetDateTime.now())
    }
}