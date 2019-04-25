package com.harishkannarao.ktor.dependency

import com.harishkannarao.ktor.api.entity.EntityApi
import com.harishkannarao.ktor.api.snippets.SnippetsApi
import com.harishkannarao.ktor.client.customer.CustomerApi
import com.harishkannarao.ktor.client.customer.CustomerClient
import com.harishkannarao.ktor.client.json.ClientJsonUtil
import com.harishkannarao.ktor.config.KtorApplicationConfig
import com.harishkannarao.ktor.dao.SimpleEntityDao
import com.harishkannarao.ktor.intercept.Interceptor
import com.harishkannarao.ktor.jdbi.JdbiFactory
import com.harishkannarao.ktor.web.user.UserWeb
import io.ktor.client.HttpClient
import io.ktor.client.engine.apache.Apache
import io.ktor.client.features.HttpPlainText

class Dependencies(
        config: KtorApplicationConfig,
        overriddenDependencies: OverriddenDependencies = OverriddenDependencies()
) {
    val client = HttpClient(Apache) {
        install(HttpPlainText) {
            defaultCharset = Charsets.UTF_8
        }

        engine {
            // For timeouts: 0 means infinite, while negative value mean to use the system's default value
            socketTimeout = 10_000  // Max time between TCP packets - default 10 seconds
            connectTimeout = 10_000 // Max time to establish an HTTP connection - default 10 seconds
            connectionRequestTimeout = 20_000 // Max time for the connection manager to start a request - 20 seconds
        }

        expectSuccess = false
        followRedirects = true
    }
    val dataSource = JdbiFactory.createHikariCpDataSource(config)
    private val jdbi = JdbiFactory.createJdbi(dataSource)
    val interceptor = Interceptor()
    private val clientJsonUtil = ClientJsonUtil()
    private val customerClient = CustomerClient(clientJsonUtil, client, config.thirdPartyCustomerServiceUrl)
    val customerApi = CustomerApi(customerClient)
    val snippetsApi: SnippetsApi = overriddenDependencies.overriddenSnippetsApi ?: SnippetsApi(config)
    val userWeb: UserWeb = UserWeb()
    private val simpleEntityDao = SimpleEntityDao(jdbi)
    val entityApi = EntityApi(simpleEntityDao)
}