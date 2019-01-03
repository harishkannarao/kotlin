package com.harishkannarao.ktor.route

import com.harishkannarao.ktor.api.session.CookieSession
import com.harishkannarao.ktor.api.session.HeaderSession
import com.harishkannarao.ktor.api.snippets.SnippetDto
import com.harishkannarao.ktor.dependency.Dependencies
import io.ktor.application.call
import io.ktor.http.ContentType
import io.ktor.http.content.PartData
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
import org.apache.commons.io.IOUtils
import org.slf4j.LoggerFactory
import java.nio.charset.StandardCharsets

class Routes(
        private val dependencies: Dependencies
) {
    private val logger = LoggerFactory.getLogger(Routes::class.java)

    val basicAuthProtected: Route.() -> Unit = {
        get("/basic-auth-get") {
            call.respondText("Successfully authenticated with basic auth", ContentType.Text.Plain)
        }
    }

    val rootPath: Route.() -> Unit = {
        get("/") {
            logger.info("sample log message to test MDC param")
            call.respondText("My Example Blog", ContentType.Text.Plain)
        }
    }

    val snippetsPath: Route.() -> Unit = {
        route("/snippets") {
            get {
                call.respond(dependencies.snippetsApi.getDefaultSnippets())
            }
            post {
                val input = call.receive<List<SnippetDto>>()
                call.respond(dependencies.snippetsApi.createSnippet(input))
            }
        }
    }

    val cookieSessionPath: Route.() -> Unit = {
        route("/cookie-session") {
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
    }

    val headerSessionPath: Route.() -> Unit = {
        route("/header-session") {
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
    }

    val fileEchoPath: Route.() -> Unit = {
        route("/file-echo") {
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
    }
}