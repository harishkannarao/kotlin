package com.harishkannarao.ktor.api.entity

import com.harishkannarao.ktor.dao.JsonEntityDao
import java.time.LocalDate
import java.util.*

class JsonEntityApi(
        private val jsonEntityDao: JsonEntityDao,
        private val jsonEntityMapper: JsonEntityMapper
) {
    fun createEntity(input: JsonEntity) {
        jsonEntityDao.createEntity(jsonEntityMapper.toJsonDbEntity(input))
    }

    fun readEntity(id: String): JsonEntity {
        return jsonEntityMapper.fromJsonDbEntity(jsonEntityDao.readEntity(UUID.fromString(id)))
    }

    fun updateEntity(input: JsonEntity) {
        jsonEntityDao.updateEntity(jsonEntityMapper.toJsonDbEntity(input))
    }

    fun deleteEntity(id: String) {
        jsonEntityDao.deleteEntity(UUID.fromString(id))
    }

    fun allEntities(): List<JsonEntity> {
        return jsonEntityDao.allEntities().map { jsonEntityMapper.fromJsonDbEntity(it) }
    }

    fun countEntities(): JsonEntityCount {
        return JsonEntityCount(jsonEntityDao.countEntities())
    }

    fun search(by: String, from: String, to: String): List<JsonEntity> {
        val dbEntities = when (by) {
            "timestamp" -> jsonEntityDao.searchByTimeStamp(from.toLong(), to.toLong())
            "date" -> jsonEntityDao.searchByDate(LocalDate.parse(from).toEpochDay(), LocalDate.parse(to).toEpochDay())
            "decimal" -> jsonEntityDao.searchByDecimal(from.toBigDecimal(), to.toBigDecimal())
            else -> emptyList()
        }
        return dbEntities.map { jsonEntityMapper.fromJsonDbEntity(it) }
    }

    fun searchByTags(commaSeperatedTags: String): List<JsonEntity> {
        val tags = commaSeperatedTags.split(",")
        return if (tags.isNotEmpty()) {
            jsonEntityDao.searchByTags(tags).map { jsonEntityMapper.fromJsonDbEntity(it) }
        } else {
            emptyList()
        }
    }
}