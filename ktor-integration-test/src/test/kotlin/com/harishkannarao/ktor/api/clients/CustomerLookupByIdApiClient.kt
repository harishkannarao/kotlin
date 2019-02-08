package com.harishkannarao.ktor.api.clients

import com.fasterxml.jackson.module.kotlin.readValue
import com.harishkannarao.ktor.api.clients.json.RestAssuredJson
import com.harishkannarao.ktor.api.clients.verifier.CustomerVerifier
import io.restassured.http.ContentType
import io.restassured.specification.RequestSpecification

class CustomerLookupByIdApiClient(requestSpecification: RequestSpecification): ApiClientBase<CustomerLookupByIdApiClient>(requestSpecification) {

    private var request: Request = Request.newRequest()

    fun withRequest(input: Request): CustomerLookupByIdApiClient {
        request = input
        return this
    }

    fun get(): CustomerLookupByIdApiClient {
        requestSpecification.basePath("/customer-by-id")
        requestSpecification.queryParam("id", request.id)
        requestSpecification.accept(ContentType.JSON)
        return doGet()
    }

    fun extractResponseEntity(): CustomerVerifier {
        val customer = RestAssuredJson.objectMapper.readValue<CustomerVerifier.Customer>(response().asString())
        return CustomerVerifier(customer)
    }

    data class Request(val id: String? = null) {
        companion object {
            fun newRequest(): Request {
                return Request()
            }
        }
    }
}