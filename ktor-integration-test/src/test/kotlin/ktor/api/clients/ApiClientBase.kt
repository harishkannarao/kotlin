package ktor.api.clients

import io.restassured.RestAssured
import io.restassured.response.Response
import io.restassured.specification.RequestSpecification

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

    protected fun response(): Response {
        return response!!
    }

    fun expectNotFoundStatus(): T {
        return expectStatusCodeToBe(404)
    }

    fun expectSuccessStatus(): T {
        return expectStatusCodeToBe(200)
    }

    private fun expectStatusCodeToBe(expectedStatus: Int): T {
        response().then().statusCode(expectedStatus)
        return this as T
    }

}