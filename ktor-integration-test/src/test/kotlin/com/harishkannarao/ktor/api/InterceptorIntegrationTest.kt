package com.harishkannarao.ktor.api

import com.harishkannarao.ktor.AbstractBaseIntegration
import org.junit.Test

class InterceptorIntegrationTest : AbstractBaseIntegration() {

        @Test
        fun `creates and reads the cookie value and returns in response`() {
            val client = clients.interceptCookieClient()
                    .get()
                    .expectSuccessStatus()

            client.expectResponseTextToBe("Known Cookie: ${client.getDecodedSessionId()}")

            clients.interceptCookieClient()
                    .withSessionCookie(client.getSessionId())
                    .get()
                    .expectNoSessionCookie()
                    .expectResponseTextToBe("Known Cookie: ${client.getDecodedSessionId()}")
        }
}