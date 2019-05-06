package com.harishkannarao.ktor.http

import io.ktor.client.HttpClient
import io.ktor.client.engine.apache.Apache
import io.ktor.client.features.HttpPlainText

object HttpClientFactory {

    fun createHttpClient(): HttpClient {
        return HttpClient(Apache) {
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
    }
}