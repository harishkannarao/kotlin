package com.harishkannarao.ktor.api.entity

import com.harishkannarao.ktor.dao.RelationalEntityDao
import com.harishkannarao.ktor.dao.entity.RelationalEntity
import java.time.OffsetDateTime
import java.util.*

class RelationalEntityApi(
        private val relationalEntityDao: RelationalEntityDao
) {
    fun createEntity(input: RelationalEntity.Data): RelationalEntity {
        val entity = RelationalEntity(UUID.randomUUID(), input)
        relationalEntityDao.createEntity(entity)
        return entity
    }

    fun getAllEntities(from: String, to: String): List<RelationalEntity> {
        val fromDate = OffsetDateTime.parse(from)
        val toDate = OffsetDateTime.parse(to)
        return relationalEntityDao.getAllEntities(fromDate, toDate)
    }

    fun readEntity(id: String): RelationalEntity {
        return relationalEntityDao.readEntity(UUID.fromString(id))
    }

    fun updateEntity(id: String, data: RelationalEntity.Data) {
        val entity = RelationalEntity(UUID.fromString(id), data)
        relationalEntityDao.updateEntity(entity)
    }

    fun deleteEntity(id: String) {
        relationalEntityDao.deleteEntity(UUID.fromString(id))
    }

}