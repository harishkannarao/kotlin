package com.harishkannarao.ktor.api

import com.harishkannarao.ktor.AbstractBaseApiIntegration
import com.harishkannarao.ktor.api.clients.JdbiJsonEntityApiClient
import com.harishkannarao.ktor.util.TestDataUtil.currentUtcOffsetDateTime
import com.harishkannarao.ktor.util.TestDataUtil.randomString
import org.testng.annotations.Test
import java.time.Instant
import java.time.LocalDate
import java.time.OffsetDateTime
import java.time.ZoneOffset


class JdbiJsonEntityIntegrationTest : AbstractBaseApiIntegration() {

    @Test
    fun `can perform CRUD operations`() {
        val initialData = JdbiJsonEntityApiClient.Entity.Data(
                username = "testUser-${randomString()}",
                timeStampInEpochMillis = currentUtcOffsetDateTime().toInstant().toEpochMilli(),
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

        val updatedData = initialData.copy(
                timeStampInEpochMillis = OffsetDateTime.now().plusDays(1).toInstant().toEpochMilli()
        )

        val id = clients.jdbiJsonEntityClient()
                .post(initialData)
                .expectCreatedStatus()
                .getEntity().id

        clients.jdbiJsonEntityClient()
                .get(id)
                .expectSuccessStatus()
                .expectEntity(JdbiJsonEntityApiClient.Entity(id, initialData))

        clients.jdbiJsonEntityClient()
                .put(JdbiJsonEntityApiClient.Entity(id, updatedData))
                .expectNoContentStatus()

        clients.jdbiJsonEntityClient()
                .delete(id)
                .expectNoContentStatus()

        clients.jdbiJsonEntityClient()
                .get(id)
                .expectNotFoundStatus()
    }

    @Test
    fun `post returns 409 for conflict on create`() {
        val initialData = JdbiJsonEntityApiClient.Entity.Data(
                username = "testUser-${randomString()}",
                timeStampInEpochMillis = currentUtcOffsetDateTime().toInstant().toEpochMilli(),
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

        clients.jdbiJsonEntityClient()
                .post(initialData)
                .expectCreatedStatus()

        clients.jdbiJsonEntityClient()
                .post(initialData)
                .expectConflictStatus()
    }

    @Test
    fun `put returns 404 for id and username mismatch on update`() {
        val firstData = JdbiJsonEntityApiClient.Entity.Data(
                username = "testUser-${randomString()}",
                timeStampInEpochMillis = currentUtcOffsetDateTime().toInstant().toEpochMilli(),
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

        val secondData = JdbiJsonEntityApiClient.Entity.Data(
                username = "testUser-${randomString()}",
                timeStampInEpochMillis = currentUtcOffsetDateTime().toInstant().toEpochMilli(),
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

        val updatedFirstData = firstData.copy(
                username = secondData.username
        )

        val firstId = clients.jdbiJsonEntityClient()
                .post(firstData)
                .expectCreatedStatus()
                .getEntity().id

        clients.jdbiJsonEntityClient()
                .post(secondData)
                .expectCreatedStatus()


        clients.jdbiJsonEntityClient()
                .put(JdbiJsonEntityApiClient.Entity(firstId, updatedFirstData))
                .expectNotFoundStatus()
    }

    @Test
    fun `can perform search operation by timestamp`() {
        val first = JdbiJsonEntityApiClient.Entity.Data(
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

        val second = first.copy(
                username = "testUser-${randomString()}",
                timeStampInEpochMillis = currentUtcOffsetDateTime().minusDays(1).toInstant().toEpochMilli()
        )

        val third = first.copy(
                username = "testUser-${randomString()}",
                timeStampInEpochMillis = currentUtcOffsetDateTime().toInstant().toEpochMilli()
        )

        val fourth = first.copy(
                username = "testUser-${randomString()}",
                timeStampInEpochMillis = currentUtcOffsetDateTime().plusDays(1).toInstant().toEpochMilli()
        )

        clients.jdbiJsonEntityClient()
                .searchByTimeStamp(
                        Instant.ofEpochMilli(second.timeStampInEpochMillis).atOffset(ZoneOffset.UTC),
                        Instant.ofEpochMilli(third.timeStampInEpochMillis).atOffset(ZoneOffset.UTC)
                )
                .expectSuccessStatus()
                .getEntities()
                .forEach {
                    clients.jdbiJsonEntityClient()
                            .delete(it.id)
                            .expectNoContentStatus()
                }

        clients.jdbiJsonEntityClient()
                .post(first)
                .expectCreatedStatus()

        val secondId = clients.jdbiJsonEntityClient()
                .post(second)
                .expectCreatedStatus()
                .getEntity().id

        val thirdId = clients.jdbiJsonEntityClient()
                .post(third)
                .expectCreatedStatus()
                .getEntity().id

        clients.jdbiJsonEntityClient()
                .post(fourth)
                .expectCreatedStatus()

        val expectedEntities = listOf(
                JdbiJsonEntityApiClient.Entity(secondId, second),
                JdbiJsonEntityApiClient.Entity(thirdId, third)
        )

        clients.jdbiJsonEntityClient()
                .searchByTimeStamp(
                        Instant.ofEpochMilli(second.timeStampInEpochMillis).atOffset(ZoneOffset.UTC),
                        Instant.ofEpochMilli(third.timeStampInEpochMillis).atOffset(ZoneOffset.UTC)
                )
                .expectSuccessStatus()
                .expectEntities(expectedEntities)
    }
}