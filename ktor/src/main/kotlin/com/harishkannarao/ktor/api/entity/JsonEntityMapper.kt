package com.harishkannarao.ktor.api.entity

import com.harishkannarao.ktor.dao.entity.JsonDbEntity
import java.time.LocalDate

class JsonEntityMapper {

    fun toJsonDbEntity(input: JsonEntity): JsonDbEntity {
        return JsonDbEntity(
                id = input.id,
                username = input.data.username,
                booleanField = input.data.booleanField,
                dateInEpochDays = input.data.date.toEpochDay(),
                decimalField = input.data.decimalField,
                intField = input.data.intField,
                tags = input.data.tags,
                timeStampInEpochMillis = input.data.timeStampInEpochMillis,
                nestedData = input.data.nestedData.map {
                    JsonDbEntity.NestedData(it.stringField, it.tags)
                }
        )
    }

    fun fromJsonDbEntity(input: JsonDbEntity): JsonEntity {
        return JsonEntity(
                id = input.id,
                data = JsonEntity.Data(
                        timeStampInEpochMillis = input.timeStampInEpochMillis,
                        intField = input.intField,
                        decimalField = input.decimalField,
                        booleanField = input.booleanField,
                        username = input.username,
                        date = LocalDate.ofEpochDay(input.dateInEpochDays),
                        tags = input.tags,
                        nestedData = input.nestedData.map {
                            JsonEntity.Data.NestedData(it.stringField, it.tags)
                        }
                )
        )
    }
}