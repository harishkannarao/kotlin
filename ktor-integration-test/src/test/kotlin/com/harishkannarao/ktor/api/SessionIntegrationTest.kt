package com.harishkannarao.ktor.api

import com.harishkannarao.ktor.AbstractBaseIntegration
import org.hamcrest.Matchers.equalTo
import org.junit.Assert.assertThat
import org.junit.Test
import java.net.URLDecoder
import java.nio.charset.StandardCharsets
import java.util.*

class SessionIntegrationTest : AbstractBaseIntegration() {

    @Test
    fun `interaction based on cookie session`() {
        val sessionId = clients.cookieSessionClient()
                .get()
                .expectResponseTextToBe("SessionId: null, counter: 1")
                .getSessionId()

        val decodedSessionId = URLDecoder.decode(sessionId, StandardCharsets.UTF_8.name())

        clients.cookieSessionClient()
                .withSessionCookie(sessionId)
                .get()
                .expectResponseTextToBe("SessionId: $decodedSessionId, counter: 2")

        clients.cookieSessionClient()
                .withSessionCookie(sessionId)
                .get()
                .expectResponseTextToBe("SessionId: $decodedSessionId, counter: 3")
    }

    // this behaviour is not ideal
    @Test
    fun `doesn't ignore preset cookie when creating a new session`() {
        val presetCookie = UUID.randomUUID().toString()
        val sessionId = clients.cookieSessionClient()
                .withSessionCookie(presetCookie)
                .get()
                .expectResponseTextToBe("SessionId: $presetCookie, counter: 1")
                .getSessionId()

        val decodedSessionId = URLDecoder.decode(sessionId, StandardCharsets.UTF_8.name())
        assertThat(decodedSessionId, equalTo(presetCookie))

        clients.cookieSessionClient()
                .withSessionCookie(sessionId)
                .get()
                .expectResponseTextToBe("SessionId: $decodedSessionId, counter: 2")
    }

    @Test
    fun `interaction based on header session`() {
        val sessionId = clients.headerSessionClient()
                .get()
                .expectResponseTextToBe("SessionId: null, counter: 1")
                .getSessionId()

        clients.headerSessionClient()
                .withSessionHeader(sessionId)
                .get()
                .expectSessionResponseHeader(sessionId)
                .expectResponseTextToBe("SessionId: $sessionId, counter: 2")

        clients.headerSessionClient()
                .withSessionHeader(sessionId)
                .get()
                .expectSessionResponseHeader(sessionId)
                .expectResponseTextToBe("SessionId: $sessionId, counter: 3")
    }

    @Test
    fun `resets header session after 9 calls`() {
        val sessionId = clients.headerSessionClient()
                .get()
                .expectResponseTextToBe("SessionId: null, counter: 1")
                .getSessionId()

        for(i in 2..9) {
            clients.headerSessionClient()
                    .withSessionHeader(sessionId)
                    .get()
                    .expectSessionResponseHeader(sessionId)
                    .expectResponseTextToBe("SessionId: $sessionId, counter: $i")
        }

        clients.headerSessionClient()
                .withSessionHeader(sessionId)
                .get()
                .expectNoSessionResponseHeader()
                .expectResponseTextToBe("Session invalidated")

        // this behaviour is not ideal as the preset request header value is used for creating session id
        clients.headerSessionClient()
                .withSessionHeader(sessionId)
                .get()
                .expectResponseTextToBe("SessionId: $sessionId, counter: 1")
                .getSessionId()

        clients.headerSessionClient()
                .withSessionHeader(sessionId)
                .get()
                .expectSessionResponseHeader(sessionId)
                .expectResponseTextToBe("SessionId: $sessionId, counter: 2")
    }
}