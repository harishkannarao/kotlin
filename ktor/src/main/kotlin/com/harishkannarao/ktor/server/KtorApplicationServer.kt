package com.harishkannarao.ktor.server

import com.fasterxml.jackson.module.kotlin.MissingKotlinParameterException
import com.harishkannarao.ktor.api.SnippetDto
import com.harishkannarao.ktor.api.SnippetsApi
import com.harishkannarao.ktor.config.KtorApplicationConfig
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.ContentNegotiation
import io.ktor.features.StatusPages
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.jackson.jackson
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.*
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import java.util.concurrent.TimeUnit

open class KtorApplicationServer(
        private val config: KtorApplicationConfig
) {

    private val snippetsApi = SnippetsApi()
    private val rootPath: Routing.() -> Unit = {
        get("/") {
            call.respondText("My Example Blog", ContentType.Text.Html)
        }
    }
    private val snippetsPath: Routing.() -> Unit = {
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
    private val myModule: Application.() -> Unit = {
        install(ContentNegotiation) {
            jackson {
            }
        }
        install(StatusPages) {
            exception<Throwable> { error ->
                if (error is MissingKotlinParameterException) {
                    call.respond(HttpStatusCode.BadRequest)
                } else {
                    call.respond(HttpStatusCode.InternalServerError)
                }
            }
        }
        routing {
            rootPath()
            if (config.enableSnippetsApi) {
                snippetsPath()
            }
        }
    }

    private val server = embeddedServer(
            factory = Netty,
            port = config.port,
            module = myModule
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