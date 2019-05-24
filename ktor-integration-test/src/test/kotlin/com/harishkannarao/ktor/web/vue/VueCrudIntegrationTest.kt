package com.harishkannarao.ktor.web.vue

import com.harishkannarao.ktor.AbstractBaseWebIntegration
import com.harishkannarao.ktor.api.clients.JdbiJsonEntityApiClient
import com.harishkannarao.ktor.util.TestDataUtil
import org.testng.annotations.Test
import java.time.LocalDate

class VueCrudIntegrationTest: AbstractBaseWebIntegration() {

    @Test
    fun `display all entities with total count`() {
        clients.jdbiJsonEntityClient()
                .get()
                .expectSuccessStatus()
                .getEntities()
                .forEach {
                    clients.jdbiJsonEntityClient()
                            .delete(it.id)
                            .expectNoContentStatus()
                }

        val first = JdbiJsonEntityApiClient.Entity.Data(
                username = "testUser-${TestDataUtil.randomString()}",
                timeStampInEpochMillis = TestDataUtil.currentUtcOffsetDateTime().minusDays(2).toInstant().toEpochMilli(),
                intField = 789,
                booleanField = true,
                decimalField = "2.5999".toBigDecimal(),
                date = LocalDate.now(),
                tags = listOf(TestDataUtil.randomString(), TestDataUtil.randomString()),
                nestedData = listOf(
                        JdbiJsonEntityApiClient.Entity.Data.NestedData(
                                stringField = TestDataUtil.randomString(),
                                tags = listOf(TestDataUtil.randomString(), TestDataUtil.randomString())
                        )
                )
        )

        val second = first.copy(
                username = "testUser-${TestDataUtil.randomString()}"
        )

        val firstId = clients.jdbiJsonEntityClient()
                .post(first)
                .expectCreatedStatus()
                .getEntity().id

        val secondId = clients.jdbiJsonEntityClient()
                .post(second)
                .expectCreatedStatus()
                .getEntity().id

        val webDriver = newWebDriver()
        webPages.vueCrudWebPage(webDriver)
                .get()
                .expectTotalEntities(2)
                .expectEntityInTable(0, firstId, first.username, first.tags)
                .expectEntityInTable(1, secondId, second.username, second.tags)
    }
}