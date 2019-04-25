package com.harishkannarao.ktor.dao

import com.harishkannarao.ktor.api.entity.SimpleEntity
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.withHandleUnchecked

class SimpleEntityDao(
        private val jdbi: Jdbi
) {

    fun getAll(): List<SimpleEntity> {
        return jdbi.withHandleUnchecked { handle ->
            handle.createQuery(GET_ALL_QUERY)
                    .mapTo(SimpleEntity::class.java)
                    .list()
        }
    }

    companion object {
        private const val GET_ALL_QUERY = "SELECT id, username FROM simple_table"
    }
}