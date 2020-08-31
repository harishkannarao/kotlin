package com.harishkannarao.ktor.dependency

import com.harishkannarao.ktor.api.entity.JsonEntityApi
import com.harishkannarao.ktor.api.entity.JsonEntityMapper
import com.harishkannarao.ktor.api.entity.RelationalEntityApi
import com.harishkannarao.ktor.api.snippets.SnippetsApi
import com.harishkannarao.ktor.client.customer.CustomerApi
import com.harishkannarao.ktor.client.customer.CustomerClient
import com.harishkannarao.ktor.client.json.ClientJsonUtil
import com.harishkannarao.ktor.client.thirdparty.ThirdPartyApiClient
import com.harishkannarao.ktor.config.KtorApplicationConfig
import com.harishkannarao.ktor.dao.JsonEntityDao
import com.harishkannarao.ktor.dao.RelationalEntityDao
import com.harishkannarao.ktor.dao.json.DbJsonUtil
import com.harishkannarao.ktor.http.HttpClientFactory
import com.harishkannarao.ktor.intercept.Interceptor
import com.harishkannarao.ktor.jdbi.JdbiFactory
import com.harishkannarao.ktor.web.user.UserWeb
import org.flywaydb.core.Flyway

class Dependencies(
        config: KtorApplicationConfig,
        overriddenDependencies: OverriddenDependencies
) {

    // third party http api clients
    val client = HttpClientFactory.createHttpClient()
    private val clientJsonUtil = ClientJsonUtil()
    private val customerClient = CustomerClient(clientJsonUtil, client, config.thirdPartyCustomerServiceUrl)
    val thirdPartyApiClient = ThirdPartyApiClient(client, config.thirdPartyApiUrl)

    // data access objects
    val dataSource = JdbiFactory.createHikariCpDataSource(config)
    private val jdbi = JdbiFactory.createJdbi(dataSource)
    private val dbJsonUtil = DbJsonUtil()
    private val relationalEntityDao = RelationalEntityDao(jdbi)
    private val jsonEntityDao = JsonEntityDao(jdbi, dbJsonUtil)
    val flyway: Flyway = Flyway.configure()
            .dataSource(dataSource)
            .locations("classpath:flyway/db/migration")
            .load()

    // interceptors
    val interceptor = Interceptor()

    // http api
    val customerApi = CustomerApi(customerClient)
    val snippetsApi: SnippetsApi = overriddenDependencies.overriddenSnippetsApi ?: SnippetsApi(config)
    val relationEntityApi = RelationalEntityApi(relationalEntityDao)
    private val jsonEntityMapper = JsonEntityMapper()
    val jsonEntityApi = JsonEntityApi(jsonEntityDao, jsonEntityMapper)

    // http web
    val userWeb: UserWeb = UserWeb()
}