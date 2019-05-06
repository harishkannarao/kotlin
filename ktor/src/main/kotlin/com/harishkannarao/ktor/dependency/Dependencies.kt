package com.harishkannarao.ktor.dependency

import com.harishkannarao.ktor.api.entity.RelationalEntityApi
import com.harishkannarao.ktor.api.snippets.SnippetsApi
import com.harishkannarao.ktor.client.customer.CustomerApi
import com.harishkannarao.ktor.client.customer.CustomerClient
import com.harishkannarao.ktor.client.json.ClientJsonUtil
import com.harishkannarao.ktor.config.KtorApplicationConfig
import com.harishkannarao.ktor.dao.RelationalEntityDao
import com.harishkannarao.ktor.dao.json.DbJsonUtil
import com.harishkannarao.ktor.http.HttpClientFactory
import com.harishkannarao.ktor.intercept.Interceptor
import com.harishkannarao.ktor.jdbi.JdbiFactory
import com.harishkannarao.ktor.web.user.UserWeb

class Dependencies(
        config: KtorApplicationConfig,
        overriddenDependencies: OverriddenDependencies = OverriddenDependencies()
) {

    // third party http api clients
    val client = HttpClientFactory.createHttpClient()
    private val clientJsonUtil = ClientJsonUtil()
    private val customerClient = CustomerClient(clientJsonUtil, client, config.thirdPartyCustomerServiceUrl)

    // data access objects
    val dataSource = JdbiFactory.createHikariCpDataSource(config)
    private val jdbi = JdbiFactory.createJdbi(dataSource)
    private val dbJsonUtil = DbJsonUtil()
    private val relationalEntityDao = RelationalEntityDao(jdbi)

    // interceptors
    val interceptor = Interceptor()

    // http api
    val customerApi = CustomerApi(customerClient)
    val snippetsApi: SnippetsApi = overriddenDependencies.overriddenSnippetsApi ?: SnippetsApi(config)
    val relationEntityApi = RelationalEntityApi(relationalEntityDao)

    // http web
    val userWeb: UserWeb = UserWeb()
}