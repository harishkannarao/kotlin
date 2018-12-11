package com.harishkannarao.ktor.api

import com.harishkannarao.ktor.AbstractBaseIntegration
import org.junit.Test

class RootPathIntegrationTest : AbstractBaseIntegration() {

    @Test
    fun `returns the text from root path`() {
        clients.rootApiClient()
                .get()
                .expectSuccessStatus()
                .expectResponseTextToBe("My Example Blog")
    }
}