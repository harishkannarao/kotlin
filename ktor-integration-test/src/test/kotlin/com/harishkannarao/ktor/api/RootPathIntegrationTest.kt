package com.harishkannarao.ktor.api

import com.harishkannarao.ktor.AbstractBaseApiIntegration
import org.testng.annotations.Test


class RootPathIntegrationTest : AbstractBaseApiIntegration() {
    @Test
    fun `returns the text from root path`() {
        clients.rootApiClient()
                .get()
                .expectSuccessStatus()
                .expectResponseTextToBe("My Example Blog")
    }
}