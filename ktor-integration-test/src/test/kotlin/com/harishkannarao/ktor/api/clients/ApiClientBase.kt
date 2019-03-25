package com.harishkannarao.ktor.api.clients

import io.restassured.RestAssured
import io.restassured.config.RedirectConfig
import io.restassured.config.RestAssuredConfig
import io.restassured.http.ContentType
import io.restassured.response.Response
import io.restassured.specification.RequestSpecification
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers
import org.hamcrest.Matchers.*

@Suppress("UNCHECKED_CAST")
abstract class ApiClientBase<T : ApiClientBase<T>>(protected val requestSpecification: RequestSpecification) {

    private var response: Response? = null
    private var followRedirect: Boolean = true

    protected fun doGet(): T {
        requestSpecification.config(createRestAssuredConfig())
        response = RestAssured.given()
                .spec(requestSpecification)
                .`when`()
                .get()
                .then()
                .extract()
                .response()

        return this as T
    }

    protected fun doHead(): T {
        requestSpecification.config(createRestAssuredConfig())
        response = RestAssured.given()
                .spec(requestSpecification)
                .`when`()
                .head()
                .then()
                .extract()
                .response()

        return this as T
    }

    protected fun doPost(): T {
        requestSpecification.config(createRestAssuredConfig())
        response = RestAssured.given()
                .spec(requestSpecification)
                .`when`()
                .post()
                .then()
                .extract()
                .response()

        return this as T
    }

    protected fun response(): Response {
        return response!!
    }

    fun withCookie(name: String, value: String): T {
        requestSpecification.cookie(name, value)
        return this as T
    }

    fun withRequestHeader(name: String, value: String): T {
        requestSpecification.header(name, value)
        return this as T
    }

    fun expectResponseTextToContain(expectedValue: String): T {
        assertThat(response().body().asString(), containsString(expectedValue))
        return this as T
    }

    fun expectResponseTextToBe(expectedValue: String): T {
        assertThat(response().body().asString(), equalTo(expectedValue))
        return this as T
    }

    private fun expectContentType(contentType: ContentType): T {
        val result = ContentType.fromContentType(response().contentType)
        assertThat(result, equalTo(contentType))
        return this as T
    }

    fun expectHtmlContentType(): T {
        return expectContentType(ContentType.HTML)
    }

    fun expectJsonContentType(): T {
        return expectContentType(ContentType.JSON)
    }

    fun expectNotFoundStatus(): T {
        return expectStatusCodeToBe(404)
    }

    fun expectPermanentlyMovedStatus(): T {
        return expectStatusCodeToBe(301)
    }

    fun isSuccessStatus(): Boolean {
        return response().statusCode == 200
    }

    fun expectSuccessStatus(): T {
        return expectStatusCodeToBe(200)
    }

    fun expectNoContentStatus(): T {
        return expectStatusCodeToBe(204)
    }

    fun expectBadRequestStatus(): T {
        return expectStatusCodeToBe(400)
    }

    fun expectUnauthorisedStatus(): T {
        return expectStatusCodeToBe(401)
    }

    fun expectLocationResponseHeader(value: String): T {
        return expectResponseHeader("Location", value)
    }

    fun expectResponseHeader(name: String, value: String): T {
        assertThat(response().headers.get(name).value, equalTo(value))
        return this as T
    }

    fun expectNoResponseHeader(name: String): T {
        assertThat(response().headers.get(name), nullValue())
        return this as T
    }

    protected fun expectJsonString(jsonPath: String, value: String): T {
        assertThat("jsonPath: $jsonPath", getJsonString(jsonPath), equalTo(value))
        return this as T
    }

    protected fun expectJsonListSizeToBe(jsonPath: String, expectedCount: Int): T {
        expectJsonPathNotToBeNull(jsonPath)
        assertThat(response!!.jsonPath().getList<Any>(jsonPath).size, equalTo(expectedCount))
        return this as T
    }

    private fun expectStatusCodeToBe(expectedStatus: Int): T {
        assertThat(response().body().asString(), response().statusCode, equalTo(expectedStatus))
        return this as T
    }

    private fun expectJsonPathNotToBeNull(jsonPath: String): T {
        assertThat("Response doesn't contain JsonPath: $jsonPath", response!!.jsonPath().getJsonObject(jsonPath), Matchers.notNullValue())
        return this as T
    }

    private fun getJsonString(jsonPath: String): String {
        expectJsonPathNotToBeNull(jsonPath)
        return response!!.jsonPath().getString(jsonPath)
    }

    fun withBasicAuth(username: String, password: String): T {
        requestSpecification.auth().preemptive().basic(username, password)
        return this as T
    }

    fun withRequestIdHeader(requestId: String): T {
        requestSpecification.header("X-Request-Id", requestId)
        return this as T
    }

    fun notFollowRedirect(): T {
        followRedirect = false
        return this as T
    }

    fun withXForwadedProtoHeaderAsHttps(): T {
        requestSpecification.header("X-Forwarded-Proto", "https")
        return this as T
    }

    fun withXForwadedProtoHeaderAsHttp(): T {
        requestSpecification.header("X-Forwarded-Proto", "http")
        return this as T
    }

    private fun createRestAssuredConfig(): RestAssuredConfig {
        return RestAssuredConfig.config()
                .redirect(createRedirectConfig())
    }

    private fun createRedirectConfig(): RedirectConfig {
        return RedirectConfig.redirectConfig().followRedirects(followRedirect)
    }

}