package com.harishkannarao.ktor.api.clients

import io.restassured.specification.RequestSpecification
import org.hamcrest.Matchers.nullValue
import org.junit.Assert.assertThat
import java.net.URLDecoder

class InterceptCookieClient(requestSpecification: RequestSpecification) : ApiClientBase<InterceptCookieClient>(requestSpecification) {

    fun get(): InterceptCookieClient {
        requestSpecification.basePath("/intercept-cookie")
        requestSpecification.accept("text/plain")

        return doGet()
    }

    fun getSessionId(): String {
        return response().cookie("KNOWN_COOKIE")
    }

    fun getDecodedSessionId(): String {
        return URLDecoder.decode(getSessionId(), Charsets.UTF_8.name())
    }

    fun withSessionCookie(value: String): InterceptCookieClient {
        return withCookie("KNOWN_COOKIE", value)
    }

    fun expectNoSessionCookie(): InterceptCookieClient {
        assertThat(response().cookie("KNOWN_COOKIE"), nullValue())
        return this
    }
}