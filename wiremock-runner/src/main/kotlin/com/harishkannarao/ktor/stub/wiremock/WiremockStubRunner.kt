package com.harishkannarao.ktor.stub.wiremock

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.core.WireMockConfiguration.options

object WiremockStubRunner {
    @JvmStatic
    fun main(args: Array<String>) {
        val wireMockServer = WireMockServer(
                options()
                        .port(8089)
                        .asynchronousResponseEnabled(true)
                        .asynchronousResponseThreads(10)
                        .disableRequestJournal()
        )
        wireMockServer.start()
        val wireMockClient = WireMock(wireMockServer)
        val wireMockStub = WireMockStub(wireMockClient)
        wireMockStub.setUpRootPath()
        val customer1 = WireMockStub.Customer.newCustomer().copy(firstName = "Test 1", lastName = "Test 2")
        val customer2 = WireMockStub.Customer.newCustomer().copy(firstName = "Test 3", lastName = "Test 4")
        val response = WireMockStub.Response.newResponse()
        wireMockStub.setUpCreateMultipleCustomers(listOf(customer1, customer2), response, 200)
        wireMockStub.setUpCreateSingleCustomer(customer1, 204)
        wireMockStub.setUpGetMultipleCustomers("Test", listOf(customer1, customer2), 200)
        wireMockStub.setUpGetSingleCustomer("1234", customer1, 200)
        println("Wiremock running at http://localhost:8089")
    }
}

