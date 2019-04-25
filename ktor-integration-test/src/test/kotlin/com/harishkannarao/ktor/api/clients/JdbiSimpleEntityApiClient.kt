package com.harishkannarao.ktor.api.clients

import io.restassured.http.ContentType
import io.restassured.specification.RequestSpecification

class JdbiSimpleEntityApiClient(requestSpecification: RequestSpecification): ApiClientBase<JdbiSimpleEntityApiClient>(requestSpecification) {

    fun get(): JdbiSimpleEntityApiClient {
        requestSpecification.basePath("/jdbi/simple-entity/get-all")
        requestSpecification.accept(ContentType.JSON)
        return doGet()
    }

    fun expectTotalEntities(size: Int): JdbiSimpleEntityApiClient {
        return expectJsonListSizeToBe("", size)
    }

    fun expectEntity(index: Int, id: String, username: String): JdbiSimpleEntityApiClient {
        expectJsonString("[$index].id", id)
        expectJsonString("[$index].username", username)
        return this
    }
}