package com.harishkannarao.ktor.api.clients.factory

import com.harishkannarao.ktor.api.clients.ListSnippetsApiClient
import io.restassured.builder.RequestSpecBuilder
import io.restassured.specification.RequestSpecification
import com.harishkannarao.ktor.api.clients.CreateSnippetApiClient

class ClientFactory(private val baseUrl: String) {

    fun listSnippetsApiClient(): ListSnippetsApiClient {
        return ListSnippetsApiClient(createRequestSpec())
    }

    fun createSnippetsApiClient(): CreateSnippetApiClient {
        return CreateSnippetApiClient(createRequestSpec())
    }

    private fun createRequestSpec(): RequestSpecification {
        return RequestSpecBuilder()
                .setBaseUri(baseUrl)
                .build()
    }
}