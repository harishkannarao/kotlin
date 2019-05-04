package com.harishkannarao.ktor.db.test.data

import com.harishkannarao.ktor.config.KtorApplicationConfig
import com.harishkannarao.ktor.dao.RelationalEntityDao
import com.harishkannarao.ktor.dao.entity.RelationalEntity
import com.harishkannarao.ktor.jdbi.JdbiFactory
import com.harishkannarao.ktor.util.DateTimeUtil
import java.math.BigDecimal
import java.time.OffsetDateTime
import java.util.*

object DbTestDataRunner {
    @JvmStatic
    fun main(args: Array<String>) {
        val defaultConfig = KtorApplicationConfig()
        val dataSource = JdbiFactory.createHikariCpDataSource(defaultConfig)
        dataSource.use { ds ->
            val jdbi = JdbiFactory.createJdbi(ds)
            val relationalEntityDao = RelationalEntityDao(jdbi)

            val referenceDateTime = DateTimeUtil.toUtcZoneOffset(OffsetDateTime.now())
            repeat(10000) {
                val entity = RelationalEntity(
                        UUID.randomUUID(),
                        RelationalEntity.Data(
                                it.toString(),
                                referenceDateTime.minusDays(it.toLong()),
                                it.toLong(),
                                it,
                                true,
                                30.00,
                                BigDecimal("35.223")
                        )

                )
                relationalEntityDao.createEntity(entity)
            }
        }
        println("Test Data setup completed")
    }
}

