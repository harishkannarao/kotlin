package com.harishkannarao.ktor.api.clients

import com.harishkannarao.ktor.api.clients.json.RestAssuredJson
import io.restassured.http.ContentType
import io.restassured.specification.RequestSpecification

class CreateSingleCustomerApiClient(requestSpecification: RequestSpecification): ApiClientBase<CreateSingleCustomerApiClient>(requestSpecification) {

    private var request: Request = Request.newRequest()

    fun withRequest(input: Request): CreateSingleCustomerApiClient {
        request = input
        return this
    }

    fun post(): CreateSingleCustomerApiClient {
        requestSpecification.basePath("/create-customer")
        val json = RestAssuredJson.objectMapper.writeValueAsString(request)
        requestSpecification.body(json)
        requestSpecification.contentType(ContentType.JSON)
        return doPost()
    }

    data class Request(val firstName: String? = null, val lastName: String? = null) {
        companion object {
            fun newRequest(): Request {
                return Request()
            }
        }
    }
}