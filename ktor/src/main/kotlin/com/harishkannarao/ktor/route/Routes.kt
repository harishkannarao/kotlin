package com.harishkannarao.ktor.route

import com.harishkannarao.ktor.api.session.CookieSession
import com.harishkannarao.ktor.api.session.HeaderSession
import com.harishkannarao.ktor.api.snippets.SnippetDto
import com.harishkannarao.ktor.config.KtorApplicationConfig
import com.harishkannarao.ktor.dependency.Dependencies
import com.harishkannarao.ktor.module.Modules
import io.ktor.application.ApplicationCallPipeline
import io.ktor.application.call
import io.ktor.auth.authenticate
import io.ktor.http.ContentType
import io.ktor.http.Cookie
import io.ktor.http.content.PartData
import io.ktor.http.content.TextContent
import io.ktor.http.content.forEachPart
import io.ktor.http.content.streamProvider
import io.ktor.request.contentType
import io.ktor.request.receive
import io.ktor.request.receiveMultipart
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.route
import io.ktor.sessions.*
import io.ktor.util.AttributeKey
import org.apache.commons.io.IOUtils
import org.slf4j.LoggerFactory
import java.lang.IllegalStateException
import java.nio.charset.StandardCharsets
import java.util.*

class Routes(
        private val dependencies: Dependencies,
        private val config: KtorApplicationConfig
) {
    private val logger = LoggerFactory.getLogger(Routes::class.java)

    val rootPath: Route.() -> Unit = {
        route("") {

            get {
                logger.info("sample log message to test MDC param")
                call.respondText("My Example Blog", ContentType.Text.Plain)
            }

            route("basic-auth-get") {
                authenticate(Modules.BASIC_AUTH) {
                    get {
                        call.respondText("Successfully authenticated with basic auth", ContentType.Text.Plain)
                    }
                }
            }

            if (config.enableSnippetsApi) {
                route("snippets") {
                    get {
                        call.respond(dependencies.snippetsApi.getDefaultSnippets())
                    }
                    post {
                        val input = call.receive<List<SnippetDto>>()
                        call.respond(dependencies.snippetsApi.createSnippet(input))
                    }
                }
            }

            route("cookie-session") {
                get {
                    val session: CookieSession = call.sessions.getOrSet(
                            generator = { CookieSession(0) }
                    )
                    val updatedSession = session.copy(value = session.value + 1)
                    call.sessions.set(updatedSession)
                    val sessionCookie: String? = call.request.cookies["COOKIE_NAME"]
                    call.respondText("SessionId: $sessionCookie, counter: ${updatedSession.value}")
                }
            }

            route("header-session") {
                get {
                    val existingSession = call.sessions.get<HeaderSession>()
                    val session: HeaderSession = when {
                        existingSession != null -> existingSession
                        else -> {
                            call.sessions.clear<HeaderSession>()
                            HeaderSession(0)
                        }
                    }
                    val updatedSession = session.copy(value = session.value + 1)
                    if (updatedSession.value > 9) {
                        call.sessions.clear<HeaderSession>()
                        call.respondText("Session invalidated")
                    } else {
                        call.sessions.clear<HeaderSession>()
                        call.sessions.set(updatedSession)
                        val sessionHeader: String? = call.request.headers["HEADER_NAME"]
                        call.respondText("SessionId: $sessionHeader, counter: ${updatedSession.value}")
                    }
                }
            }

            route("file-echo") {
                post {
                    var title = ""
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

            route("intercept-cookie") {
                intercept(ApplicationCallPipeline.Features) {
                    val knownCookie = call.request.cookies["KNOWN_COOKIE"]
                    val value = knownCookie ?: UUID.randomUUID().toString()
                    if (knownCookie == null) {
                        val cookie = Cookie("KNOWN_COOKIE", value)
                        call.response.cookies.append(cookie)
                    }
                    call.attributes.put(KNOWN_COOKIE_ATTRIBUTE_KEY, value)
                }
                get {
                    val knownCookie = call.attributes[KNOWN_COOKIE_ATTRIBUTE_KEY]
                    call.respond(TextContent("Known Cookie: $knownCookie", ContentType.Text.Plain))
                }
            }
        }
    }

    companion object {
        val KNOWN_COOKIE_ATTRIBUTE_KEY = AttributeKey<String>("KNOWN_COOKIE")
    }
}