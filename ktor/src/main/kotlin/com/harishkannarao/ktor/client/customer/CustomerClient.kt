package com.harishkannarao.ktor.client.customer

import com.harishkannarao.ktor.client.json.ClientJsonUtil
import com.harishkannarao.ktor.client.util.readTextAsUTF8
import io.ktor.client.HttpClient
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.header
import io.ktor.client.request.request
import io.ktor.client.request.url
import io.ktor.client.response.HttpResponse
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.http.fullPath
import org.apache.http.client.utils.URIBuilder
import org.slf4j.LoggerFactory

class CustomerClient(
        private val json: ClientJsonUtil,
        private val client: HttpClient,
        private val baseUrl: String
) {

    private val logger = LoggerFactory.getLogger(CustomerClient::class.java)

    suspend fun getCustomerById(id: String): CustomerDto {
        val url = URIBuilder(baseUrl)
                .setPath(GET_CUSTOMER_BY_ID_PATH)
                .addParameter(CUSTOMER_ID, id)
                .toString()
        val request: HttpRequestBuilder.() -> Unit = {
            this.url(url)
            this.method = HttpMethod.Get
        }
        val response = execute(request)
        if (response.status == HttpStatusCode.NotFound) {
            throw CustomerClientException("$id not found")
        }
        return json.fromJson(response.readTextAsUTF8())
    }

    suspend fun getCustomersByName(name: String): List<CustomerDto> {
        val url = URIBuilder(baseUrl)
                .setPath(GET_CUSTOMERS_BY_NAME_PATH)
                .addParameter(CUSTOMER_NAME, name)
                .toString()
        val request: HttpRequestBuilder.() -> Unit = {
            this.url(url)
            this.method = HttpMethod.Get
        }
        val response = execute(request)
        return json.fromJson(response.readTextAsUTF8())
    }

    suspend fun createCustomer(customer: CustomerDto) {
        val url = URIBuilder(baseUrl)
                .setPath(CREATE_SINGLE_CUSTOMER_PATH)
                .toString()
        val request: HttpRequestBuilder.() -> Unit = {
            this.url(url)
            this.method = HttpMethod.Post
            this.body = json.toJsonTextContent(customer)
        }
        execute(request)
    }

    suspend fun createCustomers(customers: List<CustomerDto>) {
        val url = URIBuilder(baseUrl)
                .setPath(CREATE_MULTIPLE_CUSTOMERS_PATH)
                .toString()
        val request: HttpRequestBuilder.() -> Unit = {
            this.url(url)
            this.method = HttpMethod.Post
            this.body = json.toJsonTextContent(customers)
        }
        execute(request)
    }

    private suspend fun execute(request: HttpRequestBuilder.() -> Unit): HttpResponse {
        val originalRequestBuilder = HttpRequestBuilder().apply(request)
        val modifiedRequest: HttpRequestBuilder.() -> Unit = {
            this.takeFrom(originalRequestBuilder)
            this.header(CUSTOM_HEADER_KEY, CUSTOM_HEADER_VALUE)
        }
        val response = client.request<HttpResponse>(modifiedRequest)

        if (logger.isDebugEnabled) {
            val requestPathWithQuery = response.call.request.url.fullPath
            val requestMethod = response.call.request.method.value
            val elapsedTimeInMillis = response.responseTime.timestamp - response.requestTime.timestamp
            logger.debug("[${response.status.value}] [$requestMethod] [$requestPathWithQuery] [$elapsedTimeInMillis]")
        }
        return response
    }

    companion object {
        private const val CUSTOM_HEADER_KEY = "X-Custom-Header"
        private const val CUSTOM_HEADER_VALUE = "SECRET_VALUE"
        private const val GET_CUSTOMER_BY_ID_PATH = "get-single-customer"
        private const val GET_CUSTOMERS_BY_NAME_PATH = "get-multiple-customers"
        private const val CREATE_SINGLE_CUSTOMER_PATH = "create-single-customer"
        private const val CREATE_MULTIPLE_CUSTOMERS_PATH = "create-multiple-customers"
        private const val CUSTOMER_ID = "customerId"
        private const val CUSTOMER_NAME = "name"
    }
}