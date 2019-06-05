package com.harishkannarao.ktor.util

import com.harishkannarao.ktor.api.clients.JdbiJsonEntityApiClient
import java.time.LocalDate
import java.time.OffsetDateTime
import java.util.*

object TestDataUtil {
    fun randomString(): String {
        return UUID.randomUUID().toString()
    }

    fun currentUtcOffsetDateTime(): OffsetDateTime {
        return DateTimeUtil.toUtcZoneOffset(OffsetDateTime.now())
    }

    fun aRandomJsonEntity(): JdbiJsonEntityApiClient.Entity.Data {
        return JdbiJsonEntityApiClient.Entity.Data(
                username = "testUser-${randomString()}",
                timeStampInEpochMillis = currentUtcOffsetDateTime().minusDays(2).toInstant().toEpochMilli(),
                intField = 789,
                booleanField = true,
                decimalField = "2.5999".toBigDecimal(),
                date = LocalDate.now(),
                tags = listOf(randomString(), randomString()),
                nestedData = listOf(
                        JdbiJsonEntityApiClient.Entity.Data.NestedData(
                                stringField = randomString(),
                                tags = listOf(randomString(), randomString())
                        )
                )
        )
    }
}