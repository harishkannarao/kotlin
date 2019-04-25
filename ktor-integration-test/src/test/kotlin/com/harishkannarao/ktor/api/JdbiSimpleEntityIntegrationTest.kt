package com.harishkannarao.ktor.api

import com.harishkannarao.ktor.AbstractBaseApiIntegration
import org.testng.annotations.Test


class JdbiSimpleEntityIntegrationTest : AbstractBaseApiIntegration() {

    @Test
    fun `returns simple entities as json`() {
        clients.jdbiSimpleEntityClient()
                .get()
                .expectSuccessStatus()
                .expectTotalEntities(1)
                .expectEntity(0, "9a7e2b9a-3340-4aa1-b2e7-c6fe85610b23", "sample_user")
    }
}