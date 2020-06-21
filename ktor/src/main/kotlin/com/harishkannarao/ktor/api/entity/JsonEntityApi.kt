package com.harishkannarao.ktor.api.entity

import com.harishkannarao.ktor.dao.JsonEntityDao
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.*

class JsonEntityApi(
        private val jsonEntityDao: JsonEntityDao,
        private val jsonEntityMapper: JsonEntityMapper
) {
    suspend fun createEntity(input: JsonEntity) = coroutineScope {
        launch { jsonEntityDao.createEntity(jsonEntityMapper.toJsonDbEntity(input)) }
    }

    suspend fun readEntityAsync(id: String): Deferred<JsonEntity> = coroutineScope {
        async { jsonEntityMapper.fromJsonDbEntity(jsonEntityDao.readEntity(UUID.fromString(id))) }
    }

    suspend fun updateEntity(input: JsonEntity) = coroutineScope {
        launch { jsonEntityDao.updateEntity(jsonEntityMapper.toJsonDbEntity(input)) }
    }

    suspend fun deleteEntityAsync(id: String) = coroutineScope {
        launch { jsonEntityDao.deleteEntity(UUID.fromString(id)) }
    }

    suspend fun allEntitiesAsync(): Deferred<List<JsonEntity>> = coroutineScope {
        async { jsonEntityDao.allEntities().map { jsonEntityMapper.fromJsonDbEntity(it) } }
    }

    suspend fun countEntitiesAsync(): Deferred<JsonEntityCount> = coroutineScope {
        async { JsonEntityCount(jsonEntityDao.countEntities()) }
    }

    suspend fun searchAsync(by: String, from: String, to: String): Deferred<List<JsonEntity>> = coroutineScope {
        async {
            val dbEntities = when (by) {
                "timestamp" -> jsonEntityDao.searchByTimeStamp(from.toLong(), to.toLong())
                "date" -> jsonEntityDao.searchByDate(LocalDate.parse(from).toEpochDay(), LocalDate.parse(to).toEpochDay())
                "decimal" -> jsonEntityDao.searchByDecimal(from.toBigDecimal(), to.toBigDecimal())
                else -> emptyList()
            }
            dbEntities.map { jsonEntityMapper.fromJsonDbEntity(it) }
        }
    }

    suspend fun searchByTagsAsync(commaSeperatedTags: String): Deferred<List<JsonEntity>> = coroutineScope {
        async {
            val tags = commaSeperatedTags.split(",")
            if (tags.isNotEmpty()) {
                jsonEntityDao.searchByTags(tags).map { jsonEntityMapper.fromJsonDbEntity(it) }
            } else {
                emptyList()
            }
        }
    }
}