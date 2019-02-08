package com.harishkannarao.ktor.client.customer

import com.harishkannarao.ktor.client.json.ClientJsonUtil
import io.ktor.client.HttpClient
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.request
import io.ktor.client.request.url
import io.ktor.client.response.HttpResponse
import io.ktor.client.response.readText
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
        return json.fromJson(response.readText(Charsets.UTF_8))
    }

    companion object {
        private const val GET_CUSTOMER_BY_ID_PATH = "get-single-customer"
        private const val CUSTOMER_ID = "customerId"
    }
}