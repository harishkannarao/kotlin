package com.harishkannarao.ktor.api.clients.factory

import io.restassured.config.ConnectionConfig
import io.restassured.config.RedirectConfig
import io.restassured.config.RestAssuredConfig

object RestAssuredUtil {

    fun createConfig(followRedirect: Boolean = true): RestAssuredConfig {
        return RestAssuredConfig
                .config()
                .connectionConfig(
                        ConnectionConfig.connectionConfig().closeIdleConnectionsAfterEachResponse()
                )
                .redirect(
                        RedirectConfig.redirectConfig().followRedirects(followRedirect)
                )
    }

}