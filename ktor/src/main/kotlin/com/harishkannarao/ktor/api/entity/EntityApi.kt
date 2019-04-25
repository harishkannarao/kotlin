package com.harishkannarao.ktor.api.entity

import com.harishkannarao.ktor.dao.SimpleEntityDao

class EntityApi(
        private val simpleEntityDao: SimpleEntityDao
) {

    fun getAll(): List<SimpleEntity> {
        return simpleEntityDao.getAll()
    }
}