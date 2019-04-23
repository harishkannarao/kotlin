package com.harishkannarao.ktor.api

import com.harishkannarao.ktor.AbstractBaseApiIntegration
import org.testng.annotations.Test

class WebjarsIntegrationTest : AbstractBaseApiIntegration() {
    @Test
    fun `returns webjars without versions`() {
        clients.webjarClient()
                .get("jquery/jquery.js")
                .expectSuccessStatus()

        clients.webjarClient()
                .get("jquery/jquery.min.js")
                .expectSuccessStatus()
    }

    @Test
    fun `returns webjars with versions`() {
        clients.webjarClient()
                .get("jquery/3.2.1/jquery.js")
                .expectSuccessStatus()

        clients.webjarClient()
                .get("jquery/3.2.1/jquery.min.js")
                .expectSuccessStatus()

    }
}