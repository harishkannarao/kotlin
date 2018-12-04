package com.harishkannarao.ktor.api.clients

import io.restassured.http.ContentType
import io.restassured.specification.RequestSpecification
import org.hamcrest.Matchers.equalTo
import org.junit.Assert.assertThat

class RootApiClient(
        requestSpecification: RequestSpecification
): ApiClientBase<RootApiClient>(requestSpecification) {

    fun get(): RootApiClient {
        requestSpecification.basePath("/")
        requestSpecification.accept(ContentType.TEXT)
        return doGet()
    }

    fun expectTextToBe(expectedValue: String): RootApiClient {
        assertThat(response().body().asString(), equalTo(expectedValue))
        return this
    }
}