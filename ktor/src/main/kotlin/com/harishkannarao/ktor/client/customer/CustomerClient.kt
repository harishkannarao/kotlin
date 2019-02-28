package com.harishkannarao.ktor.client.customer

import com.harishkannarao.ktor.client.json.ClientJsonUtil
import com.harishkannarao.ktor.client.util.readTextAsUTF8
import io.ktor.client.HttpClient
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.request
import io.ktor.client.request.url
import io.ktor.client.response.HttpResponse
import io.ktor.content.TextContent
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import org.apache.http.client.utils.URIBuilder

class CustomerClient(
        private val json: ClientJsonUtil,
        private val client: HttpClient,
        private val baseUrl: String
) {

    suspend fun getCustomerById(id: String): CustomerDto {
        val builder = URIBuilder(baseUrl)
        builder.path = GET_CUSTOMER_BY_ID_PATH
        builder.addParameter(CUSTOMER_ID, id)
        val request: HttpRequestBuilder.() -> Unit = {
            this.url(builder.toString())
            this.method = HttpMethod.Get
        }
        val response = client.request<HttpResponse>(request)
        return json.fromJson(response.readTextAsUTF8())
    }

    suspend fun getCustomersByName(name: String): List<CustomerDto> {
        val builder = URIBuilder(baseUrl)
        builder.path = GET_CUSTOMERS_BY_NAME_PATH
        builder.addParameter(CUSTOMER_NAME, name)
        val request: HttpRequestBuilder.() -> Unit = {
            this.url(builder.toString())
            this.method = HttpMethod.Get
        }
        val response = client.request<HttpResponse>(request)
        return json.fromJson(response.readTextAsUTF8())
    }

    suspend fun createCustomer(customer: CustomerDto) {
        val builder = URIBuilder(baseUrl)
        builder.path = CREATE_SINGLE_CUSTOMER_PATH
        val request: HttpRequestBuilder.() -> Unit = {
            this.url(builder.toString())
            this.method = HttpMethod.Post
            this.body = TextContent(json.toJson(customer), contentType = ContentType.Application.Json)
        }
        client.request<HttpResponse>(request)
    }

    companion object {
        private const val GET_CUSTOMER_BY_ID_PATH = "get-single-customer"
        private const val GET_CUSTOMERS_BY_NAME_PATH = "get-multiple-customers"
        private const val CREATE_SINGLE_CUSTOMER_PATH = "create-single-customer"
        private const val CUSTOMER_ID = "customerId"
        private const val CUSTOMER_NAME = "name"
    }
}