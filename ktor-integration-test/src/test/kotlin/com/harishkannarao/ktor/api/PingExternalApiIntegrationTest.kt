package com.harishkannarao.ktor.api

import com.harishkannarao.ktor.AbstractBaseApiIntegration
import org.testng.annotations.Test


class PingExternalApiIntegrationTest : AbstractBaseApiIntegration() {
    @Test
    fun `returns the url and http status from the ping external api`() {
        wireMockStub.setUpGetThirdPartyApi(200)

        clients.pingExternalApiClient()
                .get()
                .expectSuccessStatus()
                .expectJsonResponse(200, defaultConfig.thirdPartyApiUrl)
    }
}