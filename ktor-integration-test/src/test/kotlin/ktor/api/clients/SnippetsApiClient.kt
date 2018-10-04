package ktor.api.clients

import io.restassured.http.ContentType
import io.restassured.specification.RequestSpecification
import org.hamcrest.Matchers.equalTo

class SnippetsApiClient(requestSpecification: RequestSpecification): ApiClientBase<SnippetsApiClient>(requestSpecification) {

    fun get(): SnippetsApiClient {
        requestSpecification.basePath("/snippets")
        requestSpecification.accept(ContentType.JSON)

        return doGet()
    }


    fun expectOkToBe(expectedValue: Boolean) {
        response().then().body("OK", equalTo(expectedValue))
    }
}