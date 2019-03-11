package com.harishkannarao.ktor.api

import com.harishkannarao.ktor.AbstractBaseIntegration
import com.harishkannarao.ktor.api.clients.CreateMultipleCustomersApiClient
import com.harishkannarao.ktor.api.clients.CreateSingleCustomerApiClient
import com.harishkannarao.ktor.api.clients.CustomerByIdApiClient
import com.harishkannarao.ktor.api.clients.CustomersByNameApiClient
import com.harishkannarao.ktor.api.clients.verifier.Customer
import com.harishkannarao.ktor.client.customer.CustomerClient
import com.harishkannarao.ktor.rule.LogbackTestAppenderRule
import com.harishkannarao.ktor.stub.wiremock.WireMockStub
import org.junit.Rule
import org.junit.Test


class CustomerIntegrationTest : AbstractBaseIntegration() {

    @Rule
    @JvmField
    val customerClientLogger = LogbackTestAppenderRule(CustomerClient::class.java.name)

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
    fun `should search customers by name`() {
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

    @Test
    fun `should create single customer`() {
        val customer = WireMockStub.Customer.newCustomer().copy("name 1", "name 2")

        val request = CreateSingleCustomerApiClient.Request.newRequest()
                .copy(
                        firstName = customer.firstName,
                        lastName = customer.lastName
                )

        clients.createSingleCustomerApiClient()
                .withRequest(request)
                .post()
                .expectNoContentStatus()

        wireMockStub.verifyCreateSingleCustomer(customer, 1)
    }

    @Test
    fun `should create multiple customers`() {
        val customer1 = WireMockStub.Customer.newCustomer().copy("name 1", "name 2")
        val customer2 = WireMockStub.Customer.newCustomer().copy("name 3", "name 4")

        val customerList = listOf(customer1, customer2)

        val request = customerList.map {
            CreateMultipleCustomersApiClient.Customer(firstName = it.firstName, lastName = it.lastName)
        }

        clients.createMultipleCustomersApiClient()
                .withRequest(request)
                .post()
                .expectNoContentStatus()

        wireMockStub.verifyCreateMultipleCustomers(customerList, 1)
    }

    @Test
    fun `should log client requests`() {
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

        val expectedApplicationLogMessage = "[GET] [/get-single-customer?customerId=$id]"

        customerClientLogger.assertLogEntry(expectedApplicationLogMessage)
    }
}