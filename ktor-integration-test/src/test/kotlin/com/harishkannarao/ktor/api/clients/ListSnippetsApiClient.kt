package com.harishkannarao.ktor.api.clients

import io.restassured.http.ContentType
import io.restassured.specification.RequestSpecification

class ListSnippetsApiClient(requestSpecification: RequestSpecification): ApiClientBase<ListSnippetsApiClient>(requestSpecification) {

    fun get(): ListSnippetsApiClient {
        requestSpecification.basePath("/snippets")
        requestSpecification.accept(ContentType.JSON)
        return doGet()
    }

    fun expectTextToBe(index: Int, expectedValue: String): ListSnippetsApiClient {
        return expectJsonString("[$index].text", expectedValue)
    }

    fun expectSnippetsCountToBe(expectedCount: Int): ListSnippetsApiClient {
        return expectJsonListSizeToBe("", expectedCount)
    }
}