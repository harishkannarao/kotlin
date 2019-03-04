package com.harishkannarao.ktor.api.clients

import com.harishkannarao.ktor.api.clients.json.RestAssuredJson
import io.restassured.http.ContentType
import io.restassured.specification.RequestSpecification

class CreateMultipleCustomersApiClient(requestSpecification: RequestSpecification): ApiClientBase<CreateMultipleCustomersApiClient>(requestSpecification) {

    private var request: List<Customer> = emptyList()

    fun withRequest(input: List<Customer>): CreateMultipleCustomersApiClient {
        request = input
        return this
    }

    fun post(): CreateMultipleCustomersApiClient {
        requestSpecification.basePath("/create-customers")
        val json = RestAssuredJson.objectMapper.writeValueAsString(request)
        requestSpecification.body(json)
        requestSpecification.contentType(ContentType.JSON)
        return doPost()
    }

    data class Customer(val firstName: String? = null, val lastName: String? = null)
}