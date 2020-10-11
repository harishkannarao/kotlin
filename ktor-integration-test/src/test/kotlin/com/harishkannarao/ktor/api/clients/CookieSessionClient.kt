package com.harishkannarao.ktor.api.clients

import io.restassured.specification.RequestSpecification

class CookieSessionClient(requestSpecification: RequestSpecification) : ApiClientBase<CookieSessionClient>(requestSpecification) {

    fun get(): CookieSessionClient {
        requestSpecification.basePath("/session/cookie-session")
        requestSpecification.accept("text/plain")

        return doGet()
    }

    fun getSessionId(): String {
        return response().cookie("COOKIE_NAME")
    }

    fun withSessionCookie(value: String): CookieSessionClient {
        return withCookie("COOKIE_NAME", value)
    }
}