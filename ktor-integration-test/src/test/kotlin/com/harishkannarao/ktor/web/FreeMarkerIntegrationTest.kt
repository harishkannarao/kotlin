package com.harishkannarao.ktor.web

import com.harishkannarao.ktor.AbstractBaseIntegration
import org.testng.annotations.Test

class FreeMarkerIntegrationTest : AbstractBaseIntegration() {

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