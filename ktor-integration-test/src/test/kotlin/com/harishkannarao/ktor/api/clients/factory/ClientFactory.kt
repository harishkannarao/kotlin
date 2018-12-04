package com.harishkannarao.ktor.api.clients.factory

import com.harishkannarao.ktor.api.clients.ListSnippetsApiClient
import io.restassured.builder.RequestSpecBuilder
import io.restassured.specification.RequestSpecification
import com.harishkannarao.ktor.api.clients.CreateSnippetApiClient
import com.harishkannarao.ktor.api.clients.FileEchoApiClient
import com.harishkannarao.ktor.api.clients.RootApiClient
import io.restassured.filter.log.LogDetail
import io.restassured.filter.log.RequestLoggingFilter
import io.restassured.filter.log.ResponseLoggingFilter

class ClientFactory(private val baseUrl: String) {

    fun listSnippetsApiClient(): ListSnippetsApiClient {
        return ListSnippetsApiClient(createRequestSpec())
    }

    fun createSnippetsApiClient(): CreateSnippetApiClient {
        return CreateSnippetApiClient(createRequestSpec())
    }

    fun fileEchoApiClient(): FileEchoApiClient {
        return FileEchoApiClient(createRequestSpec())
    }

    fun rootApiClient(): RootApiClient {
        return RootApiClient(createRequestSpec())
    }

    private fun createRequestSpec(): RequestSpecification {
        return RequestSpecBuilder()
                .addFilter(RequestLoggingFilter(LogDetail.ALL))
                .addFilter(ResponseLoggingFilter(LogDetail.ALL))
                .setBaseUri(baseUrl)
                .build()
    }
}