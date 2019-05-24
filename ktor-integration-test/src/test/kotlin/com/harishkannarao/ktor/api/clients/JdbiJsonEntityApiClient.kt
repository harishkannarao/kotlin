package com.harishkannarao.ktor.api.clients

import com.fasterxml.jackson.module.kotlin.readValue
import com.harishkannarao.ktor.api.clients.json.RestAssuredJson
import io.restassured.http.ContentType
import io.restassured.specification.RequestSpecification
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*
import java.math.BigDecimal
import java.time.LocalDate
import java.time.OffsetDateTime
import java.util.*

class JdbiJsonEntityApiClient(requestSpecification: RequestSpecification) : ApiClientBase<JdbiJsonEntityApiClient>(requestSpecification) {

    fun get(): JdbiJsonEntityApiClient {
        requestSpecification.basePath(ENTITY_PATH)
        requestSpecification.accept(ContentType.JSON)
        return doGet()
    }

    fun get(id: UUID): JdbiJsonEntityApiClient {
        requestSpecification.basePath(ENTITY_WITH_ID_PATH)
        requestSpecification.pathParam(PARAM_ID, id.toString())
        requestSpecification.accept(ContentType.JSON)
        return doGet()
    }

    fun searchByDate(from: LocalDate, to: LocalDate): JdbiJsonEntityApiClient {
        requestSpecification.basePath(SEARCH_PATH)
        requestSpecification.queryParam("by", "date")
        requestSpecification.queryParam("from", from.toString())
        requestSpecification.queryParam("to", to.toString())
        requestSpecification.accept(ContentType.JSON)
        return doGet()
    }

    fun searchByTimeStamp(from: OffsetDateTime, to: OffsetDateTime): JdbiJsonEntityApiClient {
        requestSpecification.basePath(SEARCH_PATH)
        requestSpecification.queryParam("by", "timestamp")
        requestSpecification.queryParam("from", from.toInstant().toEpochMilli())
        requestSpecification.queryParam("to", to.toInstant().toEpochMilli())
        requestSpecification.accept(ContentType.JSON)
        return doGet()
    }

    fun searchByDecimal(from: BigDecimal, to: BigDecimal): JdbiJsonEntityApiClient {
        requestSpecification.basePath(SEARCH_PATH)
        requestSpecification.queryParam("by", "decimal")
        requestSpecification.queryParam("from", from.toString())
        requestSpecification.queryParam("to", to.toString())
        requestSpecification.accept(ContentType.JSON)
        return doGet()
    }

    fun searchByTags(tags: List<String>): JdbiJsonEntityApiClient {
        requestSpecification.basePath(SEARCH_BY_TAGS_PATH)
        requestSpecification.queryParam("tags", tags.joinToString(","))
        requestSpecification.accept(ContentType.JSON)
        return doGet()
    }

    fun post(data: Entity.Data): JdbiJsonEntityApiClient {
        requestSpecification.basePath(ENTITY_PATH)
        val json = RestAssuredJson.objectMapper.writeValueAsString(data)
        requestSpecification.body(json)
        requestSpecification.accept(ContentType.JSON)
        requestSpecification.contentType(ContentType.JSON)
        return doPost()
    }

    fun put(entity: Entity): JdbiJsonEntityApiClient {
        requestSpecification.basePath(ENTITY_WITH_ID_PATH)
        requestSpecification.pathParam(PARAM_ID, entity.id)
        val json = RestAssuredJson.objectMapper.writeValueAsString(entity.data)
        requestSpecification.body(json)
        requestSpecification.accept(ContentType.JSON)
        requestSpecification.contentType(ContentType.JSON)
        return doPut()
    }

    fun delete(id: UUID): JdbiJsonEntityApiClient {
        requestSpecification.basePath(ENTITY_WITH_ID_PATH)
        requestSpecification.pathParam(PARAM_ID, id.toString())
        requestSpecification.accept(ContentType.JSON)
        return doDelete()
    }

    fun expectEntity(expectedEntity: Entity): JdbiJsonEntityApiClient {
        val actualEntity = RestAssuredJson.objectMapper.readValue<Entity>(response().asString())
        assertEntity(actualEntity, expectedEntity)
        return this
    }

    fun expectEntities(entities: List<Entity>): JdbiJsonEntityApiClient {
        val actualEntities = RestAssuredJson.objectMapper
                .readValue<List<Entity>>(response().asString())
                .associateBy { it.id }
        assertThat(actualEntities.values.map { it.id }, containsInAnyOrder(*entities.map { it.id }.toTypedArray()))
        entities.forEach {
            assertEntity(actualEntities.getValue(it.id), it)
        }
        return this
    }

    private fun assertEntity(actual: Entity, input: Entity) {
        assertThat(actual.id, equalTo(input.id))
        assertThat(actual.data.booleanField, equalTo(input.data.booleanField))
        assertThat(actual.data.username, equalTo(input.data.username))
        assertThat(actual.data.date.toEpochDay(), equalTo(input.data.date.toEpochDay()))
        assertThat(actual.data.decimalField, equalTo(input.data.decimalField))
        assertThat(actual.data.intField, equalTo(input.data.intField))
        assertThat(actual.data.timeStampInEpochMillis, equalTo(input.data.timeStampInEpochMillis))
        assertThat(actual.data.tags, containsInAnyOrder(*input.data.tags.toTypedArray()))
        assertThat(actual.data.nestedData.size, equalTo(input.data.nestedData.size))

        val resultNestedData = actual.data.nestedData.associateBy { it.stringField }
        val inputNestedData = input.data.nestedData.associateBy { it.stringField }

        assertThat(resultNestedData.keys, containsInAnyOrder(*inputNestedData.keys.toTypedArray()))
        inputNestedData.forEach { (key, value) ->
            assertThat(resultNestedData.getValue(key).tags, containsInAnyOrder(*value.tags.toTypedArray()))
        }
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
                val date: LocalDate,
                val timeStampInEpochMillis: Long,
                val intField: Int,
                val booleanField: Boolean,
                val decimalField: BigDecimal,
                val tags: List<String> = emptyList(),
                val nestedData: List<NestedData> = emptyList()
        ) {
            data class NestedData(
                    val stringField: String,
                    val tags: List<String> = emptyList()
            )
        }
    }

    companion object {
        private const val PARAM_ID = "id"

        private const val ENTITY_PATH = "/jdbi/json-entity"
        private const val ENTITY_WITH_ID_PATH = "$ENTITY_PATH/{$PARAM_ID}"
        private const val SEARCH_PATH = "/jdbi/json-entity/search"
        private const val SEARCH_BY_TAGS_PATH = "/jdbi/json-entity/search-by-tags"
    }
}