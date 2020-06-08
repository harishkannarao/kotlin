package com.harishkannarao.ktor.client.customer

import com.harishkannarao.ktor.client.AbstractBaseClient
import com.harishkannarao.ktor.client.json.ClientJsonUtil
import io.ktor.client.HttpClient
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.url
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import org.apache.http.client.utils.URIBuilder

class CustomerClient(
        private val json: ClientJsonUtil,
        client: HttpClient,
        private val baseUrl: String
) : AbstractBaseClient(client) {

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
        return json.fromJson(readTextAsUTF8(response))
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
        return json.fromJson(readTextAsUTF8(response))
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

    companion object {
        private const val GET_CUSTOMER_BY_ID_PATH = "get-single-customer"
        private const val GET_CUSTOMERS_BY_NAME_PATH = "get-multiple-customers"
        private const val CREATE_SINGLE_CUSTOMER_PATH = "create-single-customer"
        private const val CREATE_MULTIPLE_CUSTOMERS_PATH = "create-multiple-customers"
        private const val CUSTOMER_ID = "customerId"
        private const val CUSTOMER_NAME = "name"
    }
}