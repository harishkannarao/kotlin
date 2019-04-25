package com.harishkannarao.ktor.server

import com.harishkannarao.ktor.config.KtorApplicationConfig
import com.harishkannarao.ktor.dependency.Dependencies
import com.harishkannarao.ktor.module.Modules
import io.ktor.application.Application
import io.ktor.application.ApplicationStopped
import io.ktor.server.cio.CIO
import io.ktor.server.engine.embeddedServer
import org.slf4j.LoggerFactory
import java.util.concurrent.TimeUnit

open class KtorApplicationServer(
        private val config: KtorApplicationConfig,
        modules: Modules,
        dependencies: Dependencies
) {

    private val logger = LoggerFactory.getLogger(KtorApplicationServer::class.java)
    private val stoppedEventHandler: (Application) -> Unit = {
        dependencies.client.close()
        dependencies.dataSource.close()
        logger.info("Ktor Application stopped")
    }

    private val server = embeddedServer(
            factory = CIO,
            port = config.port,
            module = modules.myModule
    )

    fun start(wait: Boolean = true) {
        server.environment.monitor.subscribe(
                definition = ApplicationStopped,
                handler = stoppedEventHandler
        )
        server.start(wait)
    }

    fun stop() {
        server.stop(
                config.shutdownGracePeriodInMillis,
                config.shutdownTimeoutInSeconds,
                TimeUnit.SECONDS
        )
    }
}