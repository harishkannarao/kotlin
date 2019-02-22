package com.harishkannarao.ktor.api

import com.harishkannarao.ktor.AbstractBaseIntegration
import com.harishkannarao.ktor.api.clients.CustomerByIdApiClient
import com.harishkannarao.ktor.api.clients.CustomersByNameApiClient
import com.harishkannarao.ktor.api.clients.verifier.Customer
import com.harishkannarao.ktor.stub.wiremock.WireMockStub
import org.junit.Test


class CustomerIntegrationTest : AbstractBaseIntegration() {

    @Test
    fun `should return customer by id`() {
        val id = "1234"

        val customer = WireMockStub.Customer.newCustomer().copy("name 1", "name 2")
        wireMockStub.setUpGetSingleCustomer(id, customer, 200)

        val expectedCustomer = Customer(customer.firstName, customer.lastName)
        val request = CustomerByIdApiClient.Request.newRequest().copy(id = id)

        clients.customerByIdApiClient()
                .withRequest(request)
                .get()
                .expectSuccessStatus()
                .extractResponseEntity()
                .expectEntity(expectedCustomer)
    }

    @Test
    fun `should return customers by`() {
        val name = "some-name"
        val customer1 = WireMockStub.Customer.newCustomer().copy("name 1", "name 2")
        val customer2 = WireMockStub.Customer.newCustomer().copy("name 3", "name 4")
        val response = listOf(customer1, customer2)
        val status = 200
        wireMockStub.setUpGetMultipleCustomers(name, response, status)

        val expectedCustomer1 = Customer(customer1.firstName, customer1.lastName)
        val expectedCustomer2 = Customer(customer2.firstName, customer2.lastName)
        val request = CustomersByNameApiClient.Request.newRequest().copy(name = name)

        clients.customersByNameApiClient()
                .withRequest(request)
                .get()
                .expectSuccessStatus()
                .extractResponseEntity()
                .expectTotalCustomersToBe(2)
                .expectCustomerAtIndex(0, expectedCustomer1)
                .expectCustomerAtIndex(1, expectedCustomer2)
    }
}