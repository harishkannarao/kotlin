package com.harishkannarao.ktor.server

import com.harishkannarao.ktor.config.KtorApplicationConfig
import com.harishkannarao.ktor.module.Modules
import io.ktor.server.cio.CIO
import io.ktor.server.engine.embeddedServer
import java.util.concurrent.TimeUnit

open class KtorApplicationServer(
        private val config: KtorApplicationConfig,
        modules: Modules
) {

    private val server = embeddedServer(
            factory = CIO,
            port = config.port,
            module = modules.myModule
    )

    fun start(wait: Boolean = true) {
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