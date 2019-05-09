package com.harishkannarao.ktor.api.entity

import com.harishkannarao.ktor.dao.entity.JsonDbEntity
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*
import org.testng.annotations.Test
import java.time.Instant
import java.time.LocalDate
import java.util.*

class JsonEntityMapperTest {
    private val subject = JsonEntityMapper()

    @Test
    fun `convert to JsonDbEntity`() {
        val input = JsonEntity(
                id = UUID.randomUUID(),
                data = JsonEntity.Data(
                        username = UUID.randomUUID().toString(),
                        date = LocalDate.now(),
                        tags = listOf(UUID.randomUUID().toString()),
                        nestedData = listOf(JsonEntity.Data.NestedData(UUID.randomUUID().toString(), listOf(UUID.randomUUID().toString()))),
                        booleanField = true,
                        decimalField = "0.223".toBigDecimal(),
                        intField = 22,
                        timeStampInEpochMillis = Instant.now().toEpochMilli()
                )
        )
        val result = subject.toJsonDbEntity(input)

        assertThat(result.id, equalTo(input.id))
        assertThat(result.booleanField, equalTo(input.data.booleanField))
        assertThat(result.username, equalTo(input.data.username))
        assertThat(result.dateInEpochDays, equalTo(input.data.date.toEpochDay()))
        assertThat(result.decimalField, equalTo(input.data.decimalField))
        assertThat(result.intField, equalTo(input.data.intField))
        assertThat(result.timeStampInEpochMillis, equalTo(input.data.timeStampInEpochMillis))
        assertThat(result.tags, containsInAnyOrder(*input.data.tags.toTypedArray()))
        assertThat(result.nestedData.size, equalTo(input.data.nestedData.size))
        input.data.nestedData.forEach { inputNestedData ->
            val found = result.nestedData.find { it.stringField == inputNestedData.stringField }
            assertThat(found, notNullValue())
            assertThat(found!!.tags, containsInAnyOrder(*inputNestedData.tags.toTypedArray()))
        }
    }

    @Test
    fun `convert from JsonDbEntity`() {
        val input = JsonDbEntity(
                id = UUID.randomUUID(),
                username = UUID.randomUUID().toString(),
                dateInEpochDays = LocalDate.now().toEpochDay(),
                tags = listOf(UUID.randomUUID().toString()),
                nestedData = listOf(JsonDbEntity.NestedData(UUID.randomUUID().toString(), listOf(UUID.randomUUID().toString()))),
                booleanField = true,
                decimalField = "0.223".toBigDecimal(),
                intField = 22,
                timeStampInEpochMillis = Instant.now().toEpochMilli()
        )
        val result = subject.fromJsonDbEntity(input)

        assertThat(result.id, equalTo(input.id))
        assertThat(result.data.booleanField, equalTo(input.booleanField))
        assertThat(result.data.username, equalTo(input.username))
        assertThat(result.data.date, equalTo(LocalDate.ofEpochDay(input.dateInEpochDays)))
        assertThat(result.data.decimalField, equalTo(input.decimalField))
        assertThat(result.data.intField, equalTo(input.intField))
        assertThat(result.data.timeStampInEpochMillis, equalTo(input.timeStampInEpochMillis))
        assertThat(result.data.tags, containsInAnyOrder(*input.tags.toTypedArray()))
        assertThat(result.data.nestedData.size, equalTo(input.nestedData.size))
        input.nestedData.forEach { inputNestedData ->
            val found = result.data.nestedData.find { it.stringField == inputNestedData.stringField }
            assertThat(found, notNullValue())
            assertThat(found!!.tags, containsInAnyOrder(*inputNestedData.tags.toTypedArray()))
        }
    }
}