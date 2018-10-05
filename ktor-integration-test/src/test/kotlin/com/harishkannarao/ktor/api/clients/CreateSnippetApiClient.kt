package com.harishkannarao.ktor.api.clients

import io.restassured.http.ContentType
import io.restassured.specification.RequestSpecification

class CreateSnippetApiClient(requestSpecification: RequestSpecification): ApiClientBase<CreateSnippetApiClient>(requestSpecification) {

    private var requestDto: TestSnippetDto = TestSnippetDto.createDefault()

    fun withText(input: String): CreateSnippetApiClient {
        requestDto = requestDto.copy(
                text = input
        )
        return this
    }

    fun post(): CreateSnippetApiClient {
        requestSpecification.basePath("/snippets")
        requestSpecification.accept(ContentType.JSON)
        requestSpecification.contentType(ContentType.JSON)
        requestSpecification.body(requestDto)
        return doPost()
    }

    fun expectTextToBe(expectedValue: String): CreateSnippetApiClient {
        return expectJsonString("text", expectedValue)
    }

    private data class TestSnippetDto(
            val text: String? = null
    ) {
        companion object {
            fun createDefault(): TestSnippetDto {
                return TestSnippetDto()
            }
        }
    }
}