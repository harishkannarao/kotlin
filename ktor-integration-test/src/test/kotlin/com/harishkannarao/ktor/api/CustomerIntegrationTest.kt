package com.harishkannarao.ktor.api

import com.harishkannarao.ktor.AbstractBaseApiIntegration
import com.harishkannarao.ktor.api.clients.*
import com.harishkannarao.ktor.api.clients.verifier.Customer
import com.harishkannarao.ktor.client.AbstractBaseClient
import com.harishkannarao.ktor.stub.wiremock.WireMockStub
import com.harishkannarao.ktor.util.LogbackTestUtil
import org.testng.annotations.AfterMethod
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test


class CustomerIntegrationTest : AbstractBaseApiIntegration() {

    private val customerClientLogger = LogbackTestUtil(AbstractBaseClient::class.java.name)

    @BeforeMethod(alwaysRun = true)
    fun setUpTestLogger() {
        customerClientLogger.setUp()
    }

    @AfterMethod(alwaysRun = true)
    fun tearDownTestLogger() {
        customerClientLogger.tearDown()
    }

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
                .extractCustomerVerifier()
                .expectEntity(expectedCustomer)
    }

    @Test
    fun `should return customers by ids`() {
        val id1 = "1234"
        val id2 = "1235"

        val customer1 = WireMockStub.Customer.newCustomer().copy("name 1", "name 2")
        wireMockStub.setUpGetSingleCustomer(id1, customer1, 200)
        val customer2 = WireMockStub.Customer.newCustomer().copy("name 3", "name 4")
        wireMockStub.setUpGetSingleCustomer(id2, customer2, 200)

        val expectedCustomer1 = Customer(customer1.firstName, customer1.lastName)
        val expectedCustomer2 = Customer(customer2.firstName, customer2.lastName)
        val request = CustomerByIdsApiClient.Request.newRequest().copy(ids = listOf(id1, id2))

        clients.customerByIdsApiClient()
                .withRequest(request)
                .get()
                .expectSuccessStatus()
                .extractCustomersVerifier()
                .expectTotalCustomersToBe(2)
                .expectCustomer(expectedCustomer1)
                .expectCustomer(expectedCustomer2)
    }

    @Test
    fun `should return bad request for not found status when looking up by ids`() {
        val id1 = "1234"
        val id2 = "1235"

        val customer1 = WireMockStub.Customer.newCustomer().copy("name 1", "name 2")
        wireMockStub.setUpGetSingleCustomer(id1, customer1, 200)
        val customer2 = WireMockStub.Customer.newCustomer().copy("name 3", "name 4")
        wireMockStub.setUpGetSingleCustomer(id2, customer2, 404)

        val request = CustomerByIdsApiClient.Request.newRequest().copy(ids = listOf(id1, id2))

        clients.customerByIdsApiClient()
                .withRequest(request)
                .get()
                .expectBadRequestStatus()
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
    fun `should log client requests for successful calls`() {
        val id = "1234"

        val customer = WireMockStub.Customer.newCustomer().copy("name 1", "name 2")
        wireMockStub.setUpGetSingleCustomer(id, customer, 200)

        val expectedCustomer = Customer(customer.firstName, customer.lastName)
        val request = CustomerByIdApiClient.Request.newRequest().copy(id = id)

        clients.customerByIdApiClient()
                .withRequest(request)
                .get()
                .expectSuccessStatus()
                .extractCustomerVerifier()
                .expectEntity(expectedCustomer)

        val expectedApplicationLogMessage = "[200] [GET] [/get-single-customer?customerId=$id]"

        customerClientLogger.assertLogEntry(expectedApplicationLogMessage)
    }

    @Test
    fun `should log client requests for failure calls`() {
        val id = "1234"

        val customer = WireMockStub.Customer.newCustomer().copy("name 1", "name 2")
        wireMockStub.setUpGetSingleCustomer(id, customer, 500)

        val expectedCustomer = Customer(customer.firstName, customer.lastName)
        val request = CustomerByIdApiClient.Request.newRequest().copy(id = id)

        clients.customerByIdApiClient()
                .withRequest(request)
                .get()
                .expectSuccessStatus()
                .extractCustomerVerifier()
                .expectEntity(expectedCustomer)

        val expectedApplicationLogMessage = "[500] [GET] [/get-single-customer?customerId=$id]"

        customerClientLogger.assertLogEntry(expectedApplicationLogMessage)
    }
}