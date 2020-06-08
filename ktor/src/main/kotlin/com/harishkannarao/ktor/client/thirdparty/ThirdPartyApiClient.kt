package com.harishkannarao.ktor.client.thirdparty

import io.ktor.client.HttpClient
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.request
import io.ktor.client.request.url
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpMethod
import org.apache.http.client.utils.URIBuilder

class ThirdPartyApiClient(
        private val client: HttpClient,
        private val baseUrl: String
) {
    suspend fun pingUrl(): Response {
        val url = URIBuilder(baseUrl).toString()
        val request: HttpRequestBuilder.() -> Unit = {
            this.url(url)
            this.method = HttpMethod.Get
        }
        val response = client.request<HttpResponse>(request)
        return Response(response.status.value, url)
    }

    data class Response(
            val status: Int,
            val url: String
    )
}