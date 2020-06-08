package com.harishkannarao.ktor.client

import io.ktor.client.HttpClient
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.header
import io.ktor.client.request.request
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.readText
import io.ktor.http.fullPath
import org.slf4j.LoggerFactory

abstract class AbstractBaseClient(
        private val client: HttpClient
) {

    private val logger = LoggerFactory.getLogger(AbstractBaseClient::class.java)

    protected suspend fun execute(request: HttpRequestBuilder.() -> Unit): HttpResponse {
        val modifiedRequest = HttpRequestBuilder().apply(request)
        modifiedRequest.header(CUSTOM_HEADER_KEY, CUSTOM_HEADER_VALUE)
        val response = client.request<HttpResponse>(modifiedRequest)
        if (logger.isDebugEnabled) {
            val requestPathWithQuery = response.call.request.url.fullPath
            val requestMethod = response.call.request.method.value
            val elapsedTimeInMillis = response.responseTime.timestamp - response.requestTime.timestamp
            logger.debug("[${response.status.value}] [$requestMethod] [$requestPathWithQuery] [$elapsedTimeInMillis]")
        }
        return response
    }

    suspend fun readTextAsUTF8(response: HttpResponse): String {
        return response.readText(Charsets.UTF_8)
    }

    companion object {
        private const val CUSTOM_HEADER_KEY = "X-Custom-Header"
        private const val CUSTOM_HEADER_VALUE = "SECRET_VALUE"
    }
}