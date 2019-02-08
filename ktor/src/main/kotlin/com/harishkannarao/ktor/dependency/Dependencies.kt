package com.harishkannarao.ktor.dependency

import com.harishkannarao.ktor.api.snippets.SnippetsApi
import com.harishkannarao.ktor.client.customer.CustomerApi
import com.harishkannarao.ktor.client.customer.CustomerClient
import com.harishkannarao.ktor.client.json.ClientJsonUtil
import com.harishkannarao.ktor.config.KtorApplicationConfig
import com.harishkannarao.ktor.intercept.Interceptor
import com.harishkannarao.ktor.web.user.UserWeb
import io.ktor.client.HttpClient
import io.ktor.client.engine.apache.Apache

class Dependencies(
        config: KtorApplicationConfig,
        overriddenDependencies: OverriddenDependencies = OverriddenDependencies()
) {
    val client = HttpClient(Apache)
    val interceptor = Interceptor()
    private val clientJsonUtil = ClientJsonUtil()
    private val customerClient = CustomerClient(clientJsonUtil, client, config.thirdPartyCustomerServiceUrl)
    val customerApi = CustomerApi(customerClient)
    val snippetsApi: SnippetsApi = overriddenDependencies.overriddenSnippetsApi ?: SnippetsApi(config)
    val userWeb: UserWeb = UserWeb()
}