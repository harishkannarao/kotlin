package com.harishkannarao.ktor.api

import com.harishkannarao.ktor.AbstractBaseIntegration
import org.junit.Test

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
        clients.rootApiClient()
                .withBasicAuth("admin", "password")
                .get()
                .expectSuccessStatus()
                .expectResponseTextToBe("My Example Blog")
    }

    @Test
    fun `returns unauthorised status when not authenticated`() {
        clients.rootApiClient()
                .get()
                .expectSuccessStatus()
                .expectResponseTextToBe("My Example Blog")
    }
}