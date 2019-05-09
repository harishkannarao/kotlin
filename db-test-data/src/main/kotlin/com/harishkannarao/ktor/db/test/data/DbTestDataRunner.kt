package com.harishkannarao.ktor.db.test.data

import com.harishkannarao.ktor.config.KtorApplicationConfig
import com.harishkannarao.ktor.dao.JsonEntityDao
import com.harishkannarao.ktor.dao.RelationalEntityDao
import com.harishkannarao.ktor.dao.entity.JsonDbEntity
import com.harishkannarao.ktor.dao.entity.RelationalEntity
import com.harishkannarao.ktor.dao.json.DbJsonUtil
import com.harishkannarao.ktor.jdbi.JdbiFactory
import com.harishkannarao.ktor.util.DateTimeUtil
import java.math.BigDecimal
import java.time.LocalDate
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
            val dbJsonUtil = DbJsonUtil()
            val jsonEntityDao = JsonEntityDao(jdbi, dbJsonUtil)

            val referenceDateTime = DateTimeUtil.toUtcZoneOffset(OffsetDateTime.now())
            val referenceDate = LocalDate.now().toEpochDay()
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

                val jsonDbEntity = JsonDbEntity(
                        id = UUID.randomUUID(),
                        username = it.toString(),
                        dateInEpochDays = referenceDate.minus(it),
                        timeStampInEpochMillis = referenceDateTime.minusDays(it.toLong()).toInstant().toEpochMilli(),
                        intField = it,
                        booleanField = true,
                        decimalField = it.toBigDecimal().add("0.001".toBigDecimal()),
                        tags = listOf(it.toString()),
                        nestedData = listOf(
                                JsonDbEntity.NestedData(it.toString(), listOf(it.toString()))
                        )
                )
                jsonEntityDao.createEntity(jsonDbEntity)
            }
        }
        println("Test Data setup completed")
    }
}

