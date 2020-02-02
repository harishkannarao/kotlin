package com.harishkannarao.ktor.api.clients.factory

import com.harishkannarao.ktor.api.clients.*
import io.restassured.builder.RequestSpecBuilder
import io.restassured.filter.log.LogDetail
import io.restassured.filter.log.RequestLoggingFilter
import io.restassured.filter.log.ResponseLoggingFilter
import io.restassured.specification.RequestSpecification

class ClientFactory(
        private val baseUrl: String,
        private val enableCallTrace: Boolean
) {

    fun listSnippetsApiClient(): ListSnippetsApiClient {
        return ListSnippetsApiClient(createRequestSpec())
    }

    fun createSnippetsApiClient(): CreateSnippetApiClient {
        return CreateSnippetApiClient(createRequestSpec())
    }

    fun customerByIdApiClient(): CustomerByIdApiClient {
        return CustomerByIdApiClient(createRequestSpec())
    }

    fun customerByIdsApiClient(): CustomerByIdsApiClient {
        return CustomerByIdsApiClient(createRequestSpec())
    }

    fun customersByNameApiClient(): CustomersByNameApiClient {
        return CustomersByNameApiClient(createRequestSpec())
    }

    fun createSingleCustomerApiClient(): CreateSingleCustomerApiClient {
        return CreateSingleCustomerApiClient(createRequestSpec())
    }

    fun createMultipleCustomersApiClient(): CreateMultipleCustomersApiClient {
        return CreateMultipleCustomersApiClient(createRequestSpec())
    }

    fun fileEchoApiClient(): FileEchoApiClient {
        return FileEchoApiClient(createRequestSpec())
    }

    fun rootApiClient(): RootApiClient {
        return RootApiClient(createRequestSpec())
    }

    fun pingExternalApiClient(): PingExternalApiClient {
        return PingExternalApiClient(createRequestSpec())
    }

    fun basicAuthApiClient(): BasicAuthApiClient {
        return BasicAuthApiClient(createRequestSpec())
    }

    fun cookieSessionClient(): CookieSessionClient {
        return CookieSessionClient(createRequestSpec())
    }

    fun headerSessionClient(): HeaderSessionClient {
        return HeaderSessionClient(createRequestSpec())
    }

    fun interceptCookieClient(): InterceptCookieClient {
        return InterceptCookieClient(createRequestSpec())
    }

    fun staticContentClient(): StaticContentClient {
        return StaticContentClient(createRequestSpec())
    }

    fun webjarClient(): WebjarClient {
        return WebjarClient(createRequestSpec())
    }

    fun jdbiRelationalEntityClient(): JdbiRelationalEntityApiClient {
        return JdbiRelationalEntityApiClient(createRequestSpec())
    }

    fun jdbiJsonEntityClient(): JdbiJsonEntityApiClient {
        return JdbiJsonEntityApiClient(createRequestSpec())
    }

    private fun createRequestSpec(): RequestSpecification {
        val requestSpecBuilder = RequestSpecBuilder().setBaseUri(baseUrl)
        if (enableCallTrace) {
            requestSpecBuilder
                    .addFilter(RequestLoggingFilter(LogDetail.ALL))
                    .addFilter(ResponseLoggingFilter(LogDetail.ALL))
        }
        return requestSpecBuilder.build()
    }
}
