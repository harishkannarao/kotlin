package com.harishkannarao.ktor.api

import com.harishkannarao.ktor.AbstractBaseApiIntegration
import com.harishkannarao.ktor.api.clients.JdbiRelationalEntityApiClient
import com.harishkannarao.ktor.util.TestDataUtil.currentUtcOffsetDateTime
import com.harishkannarao.ktor.util.TestDataUtil.randomString
import org.testng.annotations.Test
import java.math.BigDecimal
import java.time.OffsetDateTime


class JdbiRelationalEntityIntegrationTest : AbstractBaseApiIntegration() {

    @Test
    fun `can perform CRUD operations`() {
        val initialData = JdbiRelationalEntityApiClient.Entity.Data(
                username = "testUser-${randomString()}",
                dateField = currentUtcOffsetDateTime(),
                longField = 12345L,
                intField = 789,
                booleanField = true,
                doubleField = 2.5999,
                decimalField = BigDecimal(0.001)
        )

        val updatedData = initialData.copy(
                dateField = OffsetDateTime.now().plusDays(1)
        )

        val id = clients.jdbiRelationalEntityClient()
                .post(initialData)
                .expectCreatedStatus()
                .getEntity().id

        clients.jdbiRelationalEntityClient()
                .get(id)
                .expectSuccessStatus()
                .expectEntity(JdbiRelationalEntityApiClient.Entity(id, initialData))

        clients.jdbiRelationalEntityClient()
                .put(JdbiRelationalEntityApiClient.Entity(id, updatedData))
                .expectNoContentStatus()

        clients.jdbiRelationalEntityClient()
                .delete(id)
                .expectNoContentStatus()

        clients.jdbiRelationalEntityClient()
                .get(id)
                .expectNotFoundStatus()
    }

    @Test
    fun `post returns 409 for conflict on create`() {
        val initialData = JdbiRelationalEntityApiClient.Entity.Data(
                username = "testUser-${randomString()}",
                dateField = currentUtcOffsetDateTime(),
                longField = 12345L,
                intField = 789,
                booleanField = true,
                doubleField = 2.5999,
                decimalField = BigDecimal(0.001)
        )

        clients.jdbiRelationalEntityClient()
                .post(initialData)
                .expectCreatedStatus()

        clients.jdbiRelationalEntityClient()
                .post(initialData)
                .expectConflictStatus()
    }

    @Test
    fun `put returns 404 for id and username mismatch on update`() {
        val firstData = JdbiRelationalEntityApiClient.Entity.Data(
                username = "testUser-${randomString()}",
                dateField = currentUtcOffsetDateTime(),
                longField = 12345L,
                intField = 789,
                booleanField = true,
                doubleField = 2.5999,
                decimalField = BigDecimal(0.001)
        )

        val secondData = JdbiRelationalEntityApiClient.Entity.Data(
                username = "testUser-${randomString()}",
                dateField = currentUtcOffsetDateTime(),
                longField = 12345L,
                intField = 789,
                booleanField = true,
                doubleField = 2.5999,
                decimalField = BigDecimal(0.001)
        )

        val updatedFirstData = firstData.copy(
                username = secondData.username
        )

        val firstId = clients.jdbiRelationalEntityClient()
                .post(firstData)
                .expectCreatedStatus()
                .getEntity().id

        clients.jdbiRelationalEntityClient()
                .post(secondData)
                .expectCreatedStatus()


        clients.jdbiRelationalEntityClient()
                .put(JdbiRelationalEntityApiClient.Entity(firstId, updatedFirstData))
                .expectNotFoundStatus()
    }

    @Test
    fun `can perform search operation`() {
        val first = JdbiRelationalEntityApiClient.Entity.Data(
                username = "testUser-${randomString()}",
                dateField = currentUtcOffsetDateTime().minusDays(2),
                longField = 12345L,
                intField = 789,
                booleanField = true,
                doubleField = 2.5999,
                decimalField = BigDecimal(0.001)
        )

        val second = first.copy(
                username = "testUser-${randomString()}",
                dateField = currentUtcOffsetDateTime().minusDays(1)
        )

        val third = first.copy(
                username = "testUser-${randomString()}",
                dateField = currentUtcOffsetDateTime()
        )

        val fourth = first.copy(
                username = "testUser-${randomString()}",
                dateField = currentUtcOffsetDateTime().plusDays(1)
        )

        clients.jdbiRelationalEntityClient()
                .search(second.dateField, third.dateField)
                .expectSuccessStatus()
                .getEntities()
                .forEach {
                    clients.jdbiRelationalEntityClient()
                            .delete(it.id)
                            .expectNoContentStatus()
                }

        clients.jdbiRelationalEntityClient()
                .post(first)
                .expectCreatedStatus()

        val secondId = clients.jdbiRelationalEntityClient()
                .post(second)
                .expectCreatedStatus()
                .getEntity().id

        val thirdId = clients.jdbiRelationalEntityClient()
                .post(third)
                .expectCreatedStatus()
                .getEntity().id

        clients.jdbiRelationalEntityClient()
                .post(fourth)
                .expectCreatedStatus()

        val expectedEntities = listOf(
                JdbiRelationalEntityApiClient.Entity(secondId, second),
                JdbiRelationalEntityApiClient.Entity(thirdId, third)
        )

        clients.jdbiRelationalEntityClient()
                .search(second.dateField, third.dateField)
                .expectSuccessStatus()
                .expectEntities(expectedEntities)
    }
}