package com.harishkannarao.ktor.client

import com.harishkannarao.ktor.client.json.ClientJsonUtil
import io.ktor.client.HttpClient
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.header
import io.ktor.client.request.request
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.readText
import io.ktor.content.TextContent
import io.ktor.http.ContentType
import io.ktor.http.fullPath
import org.slf4j.LoggerFactory

abstract class AbstractBaseClient(
        private val json: ClientJsonUtil,
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

    @Suppress("unused")
    protected suspend fun <T> executeAndExtractObject(request: HttpRequestBuilder.() -> Unit, clazz: Class<T>): T {
        return asJsonObject(execute(request), clazz)
    }

    protected suspend fun <T> executeAndExtractList(request: HttpRequestBuilder.() -> Unit, clazz: Class<T>): List<T> {
        return asJsonList(execute(request), clazz)
    }

    @Suppress("MemberVisibilityCanBePrivate")
    protected suspend fun <T> asJsonObject(response: HttpResponse, clazz: Class<T>): T {
        return json.asJsonObject(response.readText(Charsets.UTF_8), clazz)
    }

    @Suppress("MemberVisibilityCanBePrivate")
    protected suspend fun <T> asJsonList(response: HttpResponse, clazz: Class<T>): List<T> {
        return json.asJsonList(response.readText(Charsets.UTF_8), clazz)
    }

    protected fun toJsonBody(content: Any): TextContent {
        return TextContent(json.toJson(content), contentType = ContentType.Application.Json)
    }

    companion object {
        private const val CUSTOM_HEADER_KEY = "X-Custom-Header"
        private const val CUSTOM_HEADER_VALUE = "SECRET_VALUE"
    }
}