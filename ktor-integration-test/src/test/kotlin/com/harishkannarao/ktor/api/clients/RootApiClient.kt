package com.harishkannarao.ktor.api.clients

import io.restassured.http.ContentType
import io.restassured.specification.RequestSpecification

class RootApiClient(
        requestSpecification: RequestSpecification
): ApiClientBase<RootApiClient>(requestSpecification) {

    fun get(): RootApiClient {
        requestSpecification.basePath("/")
        requestSpecification.accept(ContentType.TEXT)
        return doGet()
    }
}