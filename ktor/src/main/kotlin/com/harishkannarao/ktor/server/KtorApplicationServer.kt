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
import io.ktor.http.content.PartData
import io.ktor.http.content.forEachPart
import io.ktor.http.content.streamProvider
import io.ktor.jackson.jackson
import io.ktor.request.contentType
import io.ktor.request.receive
import io.ktor.request.receiveMultipart
import io.ktor.request.uri
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.*
import io.ktor.server.cio.CIO
import io.ktor.server.engine.embeddedServer
import org.apache.commons.io.IOUtils
import org.slf4j.LoggerFactory
import java.nio.charset.StandardCharsets
import java.util.concurrent.TimeUnit

open class KtorApplicationServer(
        private val config: KtorApplicationConfig
) {
    private val logger = LoggerFactory.getLogger(KtorApplicationServer::class.java)
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
    private val fileEchoPath: Routing.() -> Unit = {
        route("/file-echo") {
            post {
                var title: String = ""
                var fileLines: List<String> = emptyList()
                if (call.request.contentType().match(ContentType.MultiPart.Any)) {
                    val multipart = call.receiveMultipart()
                    multipart.forEachPart { part ->
                        when (part) {
                            is PartData.FormItem -> {
                                if (part.name == "title") {
                                    title = part.value
                                }
                            }
                            is PartData.FileItem -> {
                                if (part.name == "text_file") {
                                    fileLines = IOUtils.readLines(part.streamProvider(), StandardCharsets.UTF_8)
                                }
                            }
                        }
                        part.dispose()
                    }
                }
                call.respondText("title: $title, lines: $fileLines", ContentType.Text.Plain)
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
                    logger.warn(call.request.uri, error)
                    call.respond(HttpStatusCode.BadRequest)
                } else {
                    logger.error(call.request.uri, error)
                    call.respond(HttpStatusCode.InternalServerError)
                }
            }
        }
        routing {
            rootPath()
            if (config.enableSnippetsApi) {
                snippetsPath()
            }
            fileEchoPath()
        }
    }

    private val server = embeddedServer(
            factory = CIO,
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