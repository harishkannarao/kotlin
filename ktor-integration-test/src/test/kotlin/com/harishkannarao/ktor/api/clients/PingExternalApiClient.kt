package com.harishkannarao.ktor.api.clients

import io.restassured.http.ContentType
import io.restassured.specification.RequestSpecification

class PingExternalApiClient(requestSpecification: RequestSpecification)
    : ApiClientBase<PingExternalApiClient>(requestSpecification) {

    fun get(): PingExternalApiClient {
        requestSpecification.basePath("/ping-external")
        requestSpecification.accept(ContentType.JSON)
        return doGet()
    }

    fun expectJsonResponse(httpStatus: Int, externalApiUrl: String) {
        expectJsonInt("status", httpStatus)
        expectJsonString("url", externalApiUrl)
    }
}
