package com.harishkannarao.ktor.api.clients

import com.fasterxml.jackson.module.kotlin.readValue
import com.harishkannarao.ktor.api.clients.json.RestAssuredJson
import io.restassured.http.ContentType
import io.restassured.specification.RequestSpecification
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*
import java.math.BigDecimal
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.util.*

class JdbiRelationalEntityApiClient(requestSpecification: RequestSpecification) : ApiClientBase<JdbiRelationalEntityApiClient>(requestSpecification) {

    fun get(id: UUID): JdbiRelationalEntityApiClient {
        requestSpecification.basePath(ENTITY_PATH)
        requestSpecification.pathParam(PARAM_ID, id.toString())
        requestSpecification.accept(ContentType.JSON)
        return doGet()
    }

    fun search(from: OffsetDateTime, to: OffsetDateTime): JdbiRelationalEntityApiClient {
        requestSpecification.basePath(BASE_PATH)
        requestSpecification.queryParam("from", from.withOffsetSameInstant(ZoneOffset.UTC).toString())
        requestSpecification.queryParam("to", to.withOffsetSameInstant(ZoneOffset.UTC).toString())
        requestSpecification.accept(ContentType.JSON)
        return doGet()
    }

    fun post(data: Entity.Data): JdbiRelationalEntityApiClient {
        requestSpecification.basePath(BASE_PATH)
        val json = RestAssuredJson.objectMapper.writeValueAsString(data)
        requestSpecification.body(json)
        requestSpecification.accept(ContentType.JSON)
        requestSpecification.contentType(ContentType.JSON)
        return doPost()
    }

    fun put(entity: Entity): JdbiRelationalEntityApiClient {
        requestSpecification.basePath(ENTITY_PATH)
        requestSpecification.pathParam(PARAM_ID, entity.id)
        val json = RestAssuredJson.objectMapper.writeValueAsString(entity.data)
        requestSpecification.body(json)
        requestSpecification.accept(ContentType.JSON)
        requestSpecification.contentType(ContentType.JSON)
        return doPut()
    }

    fun delete(id: UUID): JdbiRelationalEntityApiClient {
        requestSpecification.basePath(ENTITY_PATH)
        requestSpecification.pathParam(PARAM_ID, id.toString())
        requestSpecification.accept(ContentType.JSON)
        return doDelete()
    }

    fun expectEntity(entity: Entity): JdbiRelationalEntityApiClient {
        val actualEntity = RestAssuredJson.objectMapper.readValue<Entity>(response().asString())
        assertThat(actualEntity, equalTo(entity))
        return this
    }

    fun expectEntities(entities: List<Entity>): JdbiRelationalEntityApiClient {
        val actualEntities = RestAssuredJson.objectMapper
                .readValue<List<Entity>>(response().asString())
                .associateBy { it.id }
        assertThat(actualEntities.values.map { it.id }, containsInAnyOrder(*entities.map { it.id }.toTypedArray()))
        entities.forEach {
            assertThat(actualEntities.getValue(it.id), equalTo(it))
        }
        return this
    }

    fun getEntities(): List<Entity> {
        return RestAssuredJson.objectMapper.readValue(response().asString())
    }

    fun getEntity(): Entity {
        return RestAssuredJson.objectMapper.readValue(response().asString())
    }

    data class Entity(
            val id: UUID,
            val data: Data
    ) {
        data class Data(
                val username: String,
                val dateField: OffsetDateTime,
                val longField: Long,
                val intField: Int,
                val booleanField: Boolean,
                val doubleField: Double,
                val decimalField: BigDecimal
        )
    }

    companion object {
        private const val BASE_PATH = "/jdbi/relational-entity"
        private const val PARAM_ID = "id"
        private const val ENTITY_PATH = "$BASE_PATH/{$PARAM_ID}"
    }
}