package com.harishkannarao.ktor.route

import com.harishkannarao.ktor.api.session.CookieSession
import com.harishkannarao.ktor.api.session.HeaderSession
import com.harishkannarao.ktor.api.snippets.SnippetDto
import com.harishkannarao.ktor.client.customer.CustomerDto
import com.harishkannarao.ktor.config.KtorApplicationConfig
import com.harishkannarao.ktor.dao.entity.RelationalEntity
import com.harishkannarao.ktor.dependency.Dependencies
import com.harishkannarao.ktor.intercept.Interceptor
import com.harishkannarao.ktor.module.Modules
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

    val rootPath: Route.() -> Unit = {
        route("") {

            get {
                logger.info("sample log message to test MDC param")
                call.respond(TextContent("My Example Blog", ContentType.Text.Plain))
            }

            route("basic-auth-get") {
                authenticate(Modules.BASIC_AUTH) {
                    get {
                        call.respond(TextContent("Successfully authenticated with basic auth", ContentType.Text.Plain))
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

            route("customer-by-id") {
                get {
                    val id = call.request.queryParameters["id"] ?: ""
                    call.respond(dependencies.customerApi.getCustomerById(id))
                }
            }

            route("customers-by-name") {
                get {
                    val name = call.request.queryParameters["name"] ?: ""
                    call.respond(dependencies.customerApi.getCustomersByName(name))
                }
            }

            route("create-customer") {
                post {
                    val input = call.receive<CustomerDto>()
                    dependencies.customerApi.createCustomer(input)
                    call.respond(HttpStatusCode.NoContent, Unit)
                }
            }

            route("create-customers") {
                post {
                    val input = call.receive<List<CustomerDto>>()
                    dependencies.customerApi.createCustomers(input)
                    call.respond(HttpStatusCode.NoContent, Unit)
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
                    call.respond(TextContent("SessionId: $sessionCookie, counter: ${updatedSession.value}", ContentType.Text.Plain))
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
                        call.respond(TextContent("Session invalidated", ContentType.Text.Plain))
                    } else {
                        call.sessions.clear<HeaderSession>()
                        call.sessions.set(updatedSession)
                        val sessionHeader: String? = call.request.headers["HEADER_NAME"]
                        call.respond(TextContent("SessionId: $sessionHeader, counter: ${updatedSession.value}", ContentType.Text.Plain))
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

            route("intercept-cookie") {
                intercept(ApplicationCallPipeline.Features, dependencies.interceptor.knownCookieInterceptor(Interceptor.KNOWN_COOKIE, Interceptor.KNOWN_COOKIE_ATTRIBUTE_KEY))
                get {
                    val knownCookie = call.attributes[Interceptor.KNOWN_COOKIE_ATTRIBUTE_KEY]
                    call.respond(TextContent("Known Cookie: $knownCookie", ContentType.Text.Plain))
                }
            }

            route("user") {
                get {
                    call.respond(dependencies.userWeb.displayUser())
                }
            }

            route("jdbi") {
                route("simple-entity") {
                    route("get-all") {
                        get {
                            call.respond(dependencies.entityApi.getAll())
                        }
                    }
                }

                route("relational-entity") {
                    get {
                        val from = call.request.queryParameters["from"]!!
                        val to = call.request.queryParameters["to"]!!
                        call.respond(dependencies.relationEntityApi.getAllEntities(from, to))
                    }
                    post {
                        val input = call.receive<RelationalEntity.Data>()
                        val createdEntity = dependencies.relationEntityApi.createEntity(input)
                        call.respond(HttpStatusCode.Created, createdEntity)
                    }
                    route("{id}") {
                        get {
                            val id = call.parameters["id"]!!
                            call.respond(dependencies.relationEntityApi.readEntity(id))
                        }
                        put {
                            val id = call.parameters["id"]!!
                            val input = call.receive<RelationalEntity.Data>()
                            dependencies.relationEntityApi.updateEntity(id, input)
                            call.respond(HttpStatusCode.NoContent, Unit)
                        }
                        delete {
                            val id = call.parameters["id"]!!
                            dependencies.relationEntityApi.deleteEntity(id)
                            call.respond(HttpStatusCode.NoContent, Unit)
                        }
                    }
                }
            }
        }
    }
}