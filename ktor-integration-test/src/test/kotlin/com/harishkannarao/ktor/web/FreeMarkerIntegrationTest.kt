package com.harishkannarao.ktor.web

import com.harishkannarao.ktor.AbstractBaseWebIntegration
import org.testng.annotations.Test

class FreeMarkerIntegrationTest : AbstractBaseWebIntegration() {

    @Test
    fun `displays user details`() {
        val webDriver = newWebDriver()
        webPages.userWebPage(webDriver)
                .get()
                .expectNameToBe("user name")
                .expectEmailToBe("user@example.com")
    }
}