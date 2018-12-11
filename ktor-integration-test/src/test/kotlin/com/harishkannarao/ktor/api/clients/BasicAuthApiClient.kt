package com.harishkannarao.ktor.api.clients

import io.restassured.http.ContentType
import io.restassured.specification.RequestSpecification

class BasicAuthApiClient(
        requestSpecification: RequestSpecification
): ApiClientBase<BasicAuthApiClient>(requestSpecification) {

    fun get(): BasicAuthApiClient {
        requestSpecification.basePath("/basic-auth-get")
        requestSpecification.accept(ContentType.TEXT)
        return doGet()
    }

}