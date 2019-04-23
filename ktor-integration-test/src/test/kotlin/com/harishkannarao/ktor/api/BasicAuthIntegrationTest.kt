package com.harishkannarao.ktor.api

import com.harishkannarao.ktor.AbstractBaseApiIntegration
import com.harishkannarao.ktor.TestGroups
import org.testng.annotations.Test

@Test(groups = [TestGroups.AUTH_API_INTEGRATION_TEST])
class BasicAuthIntegrationTest : AbstractBaseApiIntegration() {

    fun `returns message when authenticated`() {
        clients.basicAuthApiClient()
                .withBasicAuth("admin", "admin")
                .get()
                .expectSuccessStatus()
                .expectResponseTextToBe("Successfully authenticated with basic auth")
    }

    fun `returns unauthorised status with bad credentials`() {
        clients.basicAuthApiClient()
                .withBasicAuth("admin", "password")
                .get()
                .expectUnauthorisedStatus()
    }

    fun `returns unauthorised status when not authenticated`() {
        clients.basicAuthApiClient()
                .get()
                .expectUnauthorisedStatus()
    }
}