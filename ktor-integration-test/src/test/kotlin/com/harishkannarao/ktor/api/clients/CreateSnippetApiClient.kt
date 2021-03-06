package com.harishkannarao.ktor.api.clients

import io.restassured.http.ContentType
import io.restassured.specification.RequestSpecification

class CreateSnippetApiClient(requestSpecification: RequestSpecification) : ApiClientBase<CreateSnippetApiClient>(requestSpecification) {

    fun post(
            requestModifier: (TestSnippetDto) -> TestSnippetDto = { it }
    ): CreateSnippetApiClient {
        val requestDto = requestModifier(TestSnippetDto.createDefault())
        requestSpecification.basePath("/snippets")
        requestSpecification.accept(ContentType.JSON)
        requestSpecification.contentType(ContentType.JSON)
        if (requestDto.text != null) {
            requestSpecification.body(listOf(requestDto))
        }
        return doPost()
    }

    fun expectTextToBe(expectedValue: String): CreateSnippetApiClient {
        return expectJsonString("[0].text", expectedValue)
    }

    data class TestSnippetDto(
            val text: String? = null
    ) {
        companion object {
            fun createDefault(): TestSnippetDto {
                return TestSnippetDto()
            }
        }
    }
}