package com.harishkannarao.ktor.web

import com.harishkannarao.ktor.AbstractBaseWebIntegration
import org.testng.annotations.Test

class FreeMarkerIntegrationTest : AbstractBaseWebIntegration() {

    @Test
    fun `displays user details`() {
        webClients.newWebClient().use { webClient ->
            webClients.userWebClient(webClient)
                    .get()
                    .expectNameToBe("user name")
                    .expectEmailToBe("user@example.com")
        }
    }
}