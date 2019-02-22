package com.harishkannarao.ktor.api.clients

import com.fasterxml.jackson.module.kotlin.readValue
import com.harishkannarao.ktor.api.clients.json.RestAssuredJson
import com.harishkannarao.ktor.api.clients.verifier.Customer
import com.harishkannarao.ktor.api.clients.verifier.CustomersVerifier
import io.restassured.http.ContentType
import io.restassured.specification.RequestSpecification

class CustomersByNameApiClient(requestSpecification: RequestSpecification): ApiClientBase<CustomersByNameApiClient>(requestSpecification) {

    private var request: Request = Request.newRequest()

    fun withRequest(input: Request): CustomersByNameApiClient {
        request = input
        return this
    }

    fun get(): CustomersByNameApiClient {
        requestSpecification.basePath("/customers-by-name")
        requestSpecification.queryParam("name", request.name)
        requestSpecification.accept(ContentType.JSON)
        return doGet()
    }

    fun extractResponseEntity(): CustomersVerifier {
        val customers = RestAssuredJson.objectMapper.readValue<List<Customer>>(response().asString())
        return CustomersVerifier(customers)
    }

    data class Request(val name: String? = null) {
        companion object {
            fun newRequest(): Request {
                return Request()
            }
        }
    }
}