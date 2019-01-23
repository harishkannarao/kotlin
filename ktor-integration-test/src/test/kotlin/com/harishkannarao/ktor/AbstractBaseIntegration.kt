package com.harishkannarao.ktor

import com.harishkannarao.ktor.api.clients.factory.ClientFactory
import com.harishkannarao.ktor.config.KtorApplicationConfig
import com.harishkannarao.ktor.dependency.Dependencies
import com.harishkannarao.ktor.module.Modules
import com.harishkannarao.ktor.route.Routes
import com.harishkannarao.ktor.route.StaticRoutes
import com.harishkannarao.ktor.server.KtorApplicationServer
import com.harishkannarao.ktor.web.clients.factory.WebClientFactory
import org.awaitility.kotlin.await
import org.junit.Before
import java.net.ConnectException
import java.nio.channels.ClosedChannelException
import java.util.concurrent.TimeUnit

abstract class AbstractBaseIntegration {

    @Before
    fun restartServerWithDefaultConfig() {
        if (!runningWithDefaultConfig) {
            server.stop()
            server = createAndStartServerWithConfig(defaultConfig)
            runningWithDefaultConfig = true

            waitForServerToStart()
        }
    }

    protected fun restartServerWithConfig(config: KtorApplicationConfig) {
        server.stop()
        server = createAndStartServerWithConfig(config)
        runningWithDefaultConfig = false

        waitForServerToStart()
    }

    private fun waitForServerToStart() {
        await.alias("Wait for server to start")
                .atMost(4L, TimeUnit.SECONDS)
                .pollInterval(100L, TimeUnit.MILLISECONDS)
                .ignoreExceptionsMatching { throwable: Throwable ->
                    when (throwable) {
                        is ConnectException -> true
                        is ClosedChannelException -> true
                        else -> false
                    }
                }
                .until {
                    clients.rootApiClient()
                            .withXForwadedProtoHeaderAsHttps()
                            .get()
                            .isSuccessStatus()
                }
    }

    companion object {
        private var runningWithDefaultConfig = true
        val defaultConfig = KtorApplicationConfig()
        private var server: KtorApplicationServer = createAndStartServerWithConfig(defaultConfig)
        const val baseUrl = "http://localhost:8080"
        val clients: ClientFactory = ClientFactory(baseUrl)
        val webClients: WebClientFactory = WebClientFactory(baseUrl)

        private fun createAndStartServerWithConfig(config: KtorApplicationConfig): KtorApplicationServer {
            val dependencies = Dependencies(config = config)
            val routes = Routes(dependencies, config)
            val staticRoutes = StaticRoutes()
            val modules = Modules(config, routes, staticRoutes)
            val localServer = KtorApplicationServer(config, modules)
            localServer.start(wait = false)
            return localServer
        }
    }
}