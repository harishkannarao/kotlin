package com.harishkannarao.ktor.web.clients.factory

import com.gargoylesoftware.htmlunit.WebClient
import com.harishkannarao.ktor.web.clients.UserWebClient

class WebClientFactory(private val baseUrl: String) {

    fun newWebClient(): WebClient {
        return WebClient()
    }

    fun userWebClient(webClient: WebClient): UserWebClient {
        return UserWebClient(baseUrl, webClient)
    }
}