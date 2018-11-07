package com.harishkannarao.ktor.api.clients

import io.restassured.RestAssured
import io.restassured.response.Response
import io.restassured.specification.RequestSpecification
import org.hamcrest.Matchers
import org.hamcrest.Matchers.equalTo
import org.junit.Assert.assertThat

@Suppress("UNCHECKED_CAST")
abstract class ApiClientBase<T : ApiClientBase<T>>(protected val requestSpecification: RequestSpecification) {

    private var response: Response? = null

    protected fun doGet(): T {
        response = RestAssured.given()
                .spec(requestSpecification)
                .`when`()
                .get()
                .then()
                .extract()
                .response()

        return this as T
    }

    protected fun doPost(): T {
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

    fun expectNotFoundStatus(): T {
        return expectStatusCodeToBe(404)
    }

    fun expectSuccessStatus(): T {
        return expectStatusCodeToBe(200)
    }

    fun expectBadRequestStatus(): T {
        return expectStatusCodeToBe(400)
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

}