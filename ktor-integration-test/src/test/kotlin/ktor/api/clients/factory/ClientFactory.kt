package ktor.api.clients.factory

import ktor.api.clients.SnippetsApiClient
import io.restassured.builder.RequestSpecBuilder
import io.restassured.specification.RequestSpecification

class ClientFactory(private val baseUrl: String) {

    fun snippetsApiClient(): SnippetsApiClient {
        return SnippetsApiClient(createRequestSpec())
    }

    private fun createRequestSpec(): RequestSpecification {
        return RequestSpecBuilder()
                .setBaseUri(baseUrl)
                .build()
    }
}