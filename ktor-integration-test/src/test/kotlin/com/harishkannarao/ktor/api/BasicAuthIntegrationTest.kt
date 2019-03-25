package com.harishkannarao.ktor.api

import com.harishkannarao.ktor.AbstractBaseIntegration
import org.testng.annotations.Test

class BasicAuthIntegrationTest : AbstractBaseIntegration() {

    @Test
    fun `returns message when authenticated`() {
        clients.basicAuthApiClient()
                .withBasicAuth("admin", "admin")
                .get()
                .expectSuccessStatus()
                .expectResponseTextToBe("Successfully authenticated with basic auth")
    }

    @Test
    fun `returns unauthorised status with bad credentials`() {
        clients.basicAuthApiClient()
                .withBasicAuth("admin", "password")
                .get()
                .expectUnauthorisedStatus()
    }

    @Test
    fun `returns unauthorised status when not authenticated`() {
        clients.basicAuthApiClient()
                .get()
                .expectUnauthorisedStatus()
    }
}