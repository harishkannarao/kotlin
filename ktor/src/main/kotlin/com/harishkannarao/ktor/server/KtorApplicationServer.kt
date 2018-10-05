package com.harishkannarao.ktor.server

import com.harishkannarao.ktor.api.SnippetDto
import com.harishkannarao.ktor.api.SnippetsApi
import com.harishkannarao.ktor.config.KtorApplicationConfig
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.jackson.jackson
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.route
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.netty.NettyApplicationEngine
import java.util.concurrent.TimeUnit

open class KtorApplicationServer(
        private val config: KtorApplicationConfig
) {

    private val snippetsApi = SnippetsApi()
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
                if (config.enableSnippetsApi) {
                    route("/snippets") {
                        get {
                            call.respond(snippetsApi.getDefaultSnippets())
                        }
                        post {
                            val input = call.receive<SnippetDto>()
                            call.respond(snippetsApi.createSnippet(input))
                        }
                    }
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