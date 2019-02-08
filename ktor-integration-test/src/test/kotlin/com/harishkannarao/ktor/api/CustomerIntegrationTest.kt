package com.harishkannarao.ktor.api

import com.harishkannarao.ktor.AbstractBaseIntegration
import com.harishkannarao.ktor.api.clients.CustomerLookupByIdApiClient
import com.harishkannarao.ktor.api.clients.verifier.CustomerVerifier
import com.harishkannarao.ktor.stub.wiremock.WireMockStub
import org.junit.Test


class CustomerIntegrationTest : AbstractBaseIntegration() {

    @Test
    fun `should return customer by id`() {
        val id = "1234"

        val stubResponse = WireMockStub.Customer.newCustomer().copy("name 1", "name 2")
        wireMockStub.setUpGetSingleCustomer(id, stubResponse, 200)

        val expectedResponse = CustomerVerifier.Customer(stubResponse.firstName, stubResponse.lastName)
        val request = CustomerLookupByIdApiClient.Request.newRequest()
                .copy(id = id)

        clients.customerLookupByIdApiClient()
                .withRequest(request)
                .get()
                .expectSuccessStatus()
                .extractResponseEntity()
                .expectEntity(expectedResponse)
    }


}