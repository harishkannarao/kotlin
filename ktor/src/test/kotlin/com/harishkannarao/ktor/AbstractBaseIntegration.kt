package com.harishkannarao.ktor

import com.harishkannarao.ktor.api.clients.factory.ClientFactory
import com.harishkannarao.ktor.config.KtorApplicationConfig
import org.junit.Before

abstract class AbstractBaseIntegration {

    @Before
    fun restartServerWithDefaultConfig() {
        if (!runningWithDefaultConfig) {
            server.stop()
            server = createAndStartServerWithConfig(defaultConfig)
            runningWithDefaultConfig = true
        }
    }

    protected fun restartServerWithConfig(config: KtorApplicationConfig) {
        server.stop()
        server = createAndStartServerWithConfig(config)
        runningWithDefaultConfig = false
    }

    companion object {
        private var runningWithDefaultConfig = true
        val defaultConfig = KtorApplicationConfig()
        private var server: KtorTestApplicationServer = createAndStartServerWithConfig(defaultConfig)
        val clients: ClientFactory = ClientFactory("http://localhost:8080")

        private fun createAndStartServerWithConfig(config: KtorApplicationConfig): KtorTestApplicationServer {
            val localServer = KtorTestApplicationServer(config)
            localServer.start(wait = false)
            return localServer
        }
    }
}