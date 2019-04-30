package com.harishkannarao.ktor.jdbi

import com.harishkannarao.ktor.config.KtorApplicationConfig
import com.harishkannarao.ktor.dao.mapper.SimpleEntityRowMapper
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.KotlinPlugin
import org.jdbi.v3.postgres.PostgresPlugin
import org.jdbi.v3.sqlobject.SqlObjectPlugin
import org.jdbi.v3.sqlobject.kotlin.KotlinSqlObjectPlugin

object JdbiFactory {

    fun createHikariCpDataSource(appConfig: KtorApplicationConfig): HikariDataSource {
        val config = HikariConfig()
        config.jdbcUrl = appConfig.jdbcUrl
        config.username = appConfig.jdbcUserName
        config.password = appConfig.jdbcPassword
        config.driverClassName = "org.postgresql.Driver"
        config.addDataSourceProperty("cachePrepStmts", "true")
        config.addDataSourceProperty("prepStmtCacheSize", "250")
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048")

        return HikariDataSource(config)
    }

    fun createJdbi(dataSource: HikariDataSource): Jdbi {
        val jdbi = Jdbi.create(dataSource)
        jdbi.installPlugin(SqlObjectPlugin())
        jdbi.installPlugin(KotlinPlugin())
        jdbi.installPlugin(KotlinSqlObjectPlugin())
        jdbi.installPlugin(PostgresPlugin())

        jdbi.registerRowMapper(SimpleEntityRowMapper())

        return jdbi
    }
}