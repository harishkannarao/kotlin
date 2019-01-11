package com.harishkannarao.ktor.api.clients

import io.restassured.specification.RequestSpecification

class StaticContentClient(requestSpecification: RequestSpecification) : ApiClientBase<StaticContentClient>(requestSpecification) {

    fun get(path: String): StaticContentClient {
        requestSpecification.basePath(path)
        return doGet()
    }

    fun head(path: String): StaticContentClient {
        requestSpecification.basePath(path)
        return doHead()
    }
}