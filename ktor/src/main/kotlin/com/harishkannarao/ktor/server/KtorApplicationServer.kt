package com.harishkannarao.ktor.server

import com.harishkannarao.ktor.config.KtorApplicationConfig
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.jackson.jackson
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.netty.NettyApplicationEngine
import java.util.concurrent.TimeUnit

class KtorApplicationServer(private val config: KtorApplicationConfig) {

    private val server = createServer()

    private fun createServer(): NettyApplicationEngine {
        return embeddedServer(Netty, config.port) {
            install(ContentNegotiation) {
                jackson {
                }
            }
            routing {
                get("/") {
                    call.respondText("My Example Blog", ContentType.Text.Html)
                }
                get("/snippets") {
                    call.respond(mapOf("OK" to true))
                }
            }
        }
    }

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