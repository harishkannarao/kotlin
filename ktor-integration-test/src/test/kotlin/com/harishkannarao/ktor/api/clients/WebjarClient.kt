package com.harishkannarao.ktor.api.clients

import io.restassured.specification.RequestSpecification

class WebjarClient(requestSpecification: RequestSpecification) : ApiClientBase<WebjarClient>(requestSpecification) {

    fun get(path: String): WebjarClient {
        requestSpecification.basePath("/webjars/$path")
        return doGet()
    }
}