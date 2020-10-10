package com.harishkannarao.ktor.route

import com.harishkannarao.ktor.api.session.CookieSession
import com.harishkannarao.ktor.api.session.HeaderSession
import com.harishkannarao.ktor.api.snippets.SnippetDto
import com.harishkannarao.ktor.client.customer.CustomerDto
import com.harishkannarao.ktor.config.KtorApplicationConfig
import com.harishkannarao.ktor.dependency.Dependencies
import com.harishkannarao.ktor.feature.Features
import com.harishkannarao.ktor.intercept.Interceptor
import io.ktor.application.ApplicationCallPipeline
import io.ktor.application.call
import io.ktor.auth.authenticate
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.charset
import io.ktor.http.content.PartData
import io.ktor.http.content.TextContent
import io.ktor.http.content.forEachPart
import io.ktor.http.content.streamProvider
import io.ktor.request.contentType
import io.ktor.request.receive
import io.ktor.request.receiveMultipart
import io.ktor.response.respond
import io.ktor.routing.*
import io.ktor.sessions.*
import org.apache.commons.io.IOUtils
import org.slf4j.LoggerFactory
import java.nio.charset.StandardCharsets

class Routes(
        private val dependencies: Dependencies,
        private val config: KtorApplicationConfig
) {
    private val logger = LoggerFactory.getLogger(Routes::class.java)

    fun configure(routing: Routing) {
        routing.route("/") {

            get {
                logger.info("sample log message to test MDC param")
                call.respond(TextContent("My Example Blog", ContentType.Text.Plain))
            }

            route("/basic-auth-get") {
                authenticate(Features.BASIC_AUTH) {
                    get {
                        call.respond(TextContent("Successfully authenticated with basic auth", ContentType.Text.Plain))
                    }
                }
            }

            if (config.enableSnippetsApi) {
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
        }

        routing.route("/customer-by-id") {
            get {
                val id = call.request.queryParameters["id"] ?: ""
                call.respond(dependencies.customerApi.getCustomerById(id))
            }
        }

        routing.route("/customers-by-ids") {
            get {
                val ids = call.request.queryParameters["ids"] ?: ""
                call.respond(dependencies.customerApi.getCustomerByIds(ids.split(",")))
            }
        }

        routing.route("/customers-by-name") {
            get {
                val name = call.request.queryParameters["name"] ?: ""
                call.respond(dependencies.customerApi.getCustomersByName(name))
            }
        }

        routing.route("/create-customer") {
            post {
                val input = call.receive<CustomerDto>()
                dependencies.customerApi.createCustomer(input)
                call.respond(HttpStatusCode.NoContent, Unit)
            }
        }

        routing.route("/create-customers") {
            post {
                val input = call.receive<List<CustomerDto>>()
                dependencies.customerApi.createCustomers(input)
                call.respond(HttpStatusCode.NoContent, Unit)
            }
        }

        routing.route("cookie-session") {
            get {
                val session: CookieSession = call.sessions.getOrSet(
                        generator = { CookieSession(0) }
                )
                val updatedSession = session.copy(value = session.value + 1)
                call.sessions.set(updatedSession)
                val sessionCookie: String? = call.request.cookies["COOKIE_NAME"]
                call.respond(TextContent("SessionId: $sessionCookie, counter: ${updatedSession.value}", ContentType.Text.Plain))
            }
        }

        routing.route("header-session") {
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
                    call.respond(TextContent("Session invalidated", ContentType.Text.Plain))
                } else {
                    call.sessions.clear<HeaderSession>()
                    call.sessions.set(updatedSession)
                    val sessionHeader: String? = call.request.headers["HEADER_NAME"]
                    call.respond(TextContent("SessionId: $sessionHeader, counter: ${updatedSession.value}", ContentType.Text.Plain))
                }
            }
        }

        routing.route("file-echo") {
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
                                    val charset = part.contentType?.charset() ?: StandardCharsets.UTF_8
                                    part.streamProvider().use { inputStream ->
                                        fileLines = IOUtils.readLines(inputStream, charset)
                                    }
                                }
                            }
                        }
                        part.dispose()
                    }
                }
                call.respond(TextContent("title: $title, lines: $fileLines", ContentType.Text.Plain))
            }
        }

        routing.route("intercept-cookie") {
            intercept(ApplicationCallPipeline.Features, dependencies.interceptor.knownCookieInterceptor(Interceptor.KNOWN_COOKIE, Interceptor.KNOWN_COOKIE_ATTRIBUTE_KEY))
            get {
                val knownCookie = call.attributes[Interceptor.KNOWN_COOKIE_ATTRIBUTE_KEY]
                call.respond(TextContent("Known Cookie: $knownCookie", ContentType.Text.Plain))
            }
        }

        routing.route("user") {
            get {
                call.respond(dependencies.userWeb.displayUser())
            }
        }

        routing.route("generate-401") {
            get {
                call.respond(HttpStatusCode.Unauthorized, Unit)
            }
        }

        routing.route("ping-external") {
            get {
                call.respond(HttpStatusCode.OK, dependencies.thirdPartyApiClient.pingUrl())
            }
        }
    }
}