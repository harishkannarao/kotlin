package com.harishkannarao.ktor.api.clients

import com.fasterxml.jackson.module.kotlin.readValue
import com.harishkannarao.ktor.api.clients.json.RestAssuredJson
import com.harishkannarao.ktor.api.clients.verifier.Customer
import com.harishkannarao.ktor.api.clients.verifier.CustomersVerifier
import io.restassured.http.ContentType
import io.restassured.specification.RequestSpecification

class CustomerByIdsApiClient(requestSpecification: RequestSpecification): ApiClientBase<CustomerByIdsApiClient>(requestSpecification) {

    private var request: Request = Request.newRequest()

    fun withRequest(input: Request): CustomerByIdsApiClient {
        request = input
        return this
    }

    fun get(): CustomerByIdsApiClient {
        requestSpecification.basePath("/customers-by-ids")
        requestSpecification.queryParam("ids", request.ids.joinToString(","))
        requestSpecification.accept(ContentType.JSON)
        return doGet()
    }

    fun extractCustomersVerifier(): CustomersVerifier {
        val customers = RestAssuredJson.objectMapper.readValue<List<Customer>>(response().asString())
        return CustomersVerifier(customers)
    }

    data class Request(val ids: List<String> = emptyList()) {
        companion object {
            fun newRequest(): Request {
                return Request()
            }
        }
    }
}