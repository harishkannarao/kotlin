package com.harishkannarao.ktor.api.clients

import io.restassured.specification.RequestSpecification

class HeaderSessionClient(requestSpecification: RequestSpecification) : ApiClientBase<HeaderSessionClient>(requestSpecification) {

    fun get(): HeaderSessionClient {
        requestSpecification.basePath("/session/header-session")
        requestSpecification.accept("text/plain")

        return doGet()
    }

    fun getSessionId(): String {
        return response().header("HEADER_NAME")
    }

    fun withSessionHeader(value: String): HeaderSessionClient {
        return withRequestHeader("HEADER_NAME", value)
    }

    fun expectSessionResponseHeader(value: String): HeaderSessionClient {
        return expectResponseHeader("HEADER_NAME", value)
    }

    fun expectNoSessionResponseHeader(): HeaderSessionClient {
        return expectNoResponseHeader("HEADER_NAME")
    }
}