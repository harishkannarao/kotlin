package com.harishkannarao.ktor.feature

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule
import com.harishkannarao.ktor.api.session.CookieSession
import com.harishkannarao.ktor.api.session.HeaderSession
import com.harishkannarao.ktor.client.customer.CustomerClientException
import com.harishkannarao.ktor.config.KtorApplicationConfig
import com.harishkannarao.ktor.dao.exception.DbEntityConflictException
import com.harishkannarao.ktor.dao.exception.DbEntityNotFoundException
import com.harishkannarao.ktor.dependency.Dependencies
import com.harishkannarao.ktor.intercept.Interceptor
import com.harishkannarao.ktor.route.LocationRoutes
import com.harishkannarao.ktor.route.Routes
import com.harishkannarao.ktor.route.StaticRoutes
import freemarker.cache.ClassTemplateLoader
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.features.*
import io.ktor.freemarker.*
import io.ktor.http.*
import io.ktor.jackson.*
import io.ktor.locations.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.sessions.*
import io.ktor.webjars.*
import org.slf4j.LoggerFactory
import org.slf4j.event.Level
import java.util.*

object Features {

    const val BASIC_AUTH = "my-basic-auth"
    private val LOG = LoggerFactory.getLogger(Features::class.java)

    fun installContentNegotiation(application: Application) {
        application.install(ContentNegotiation) {
            jackson {
                this.registerModule(ParameterNamesModule())
                this.registerModule(Jdk8Module())
                this.registerModule(JavaTimeModule())
                this.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
            }
        }
    }

    fun installWebJars(application: Application) {
        @Suppress("EXPERIMENTAL_API_USAGE")
        application.install(Webjars)
    }

    fun installAutoHeadResponse(application: Application) {
        application.install(AutoHeadResponse)
    }

    fun installFreeMarker(application: Application, config: KtorApplicationConfig) {
        application.install(FreeMarker) {
            this.templateLoader = ClassTemplateLoader(Features::class.java.classLoader, "templates")
            if (config.developmentMode) {
                this.setSharedVariable("javaScriptVariant", "")
                this.setSharedVariable("cssVariant", "")
            } else {
                this.setSharedVariable("javaScriptVariant", ".min")
                this.setSharedVariable("cssVariant", ".min")
            }
        }
    }

    fun installSessions(application: Application) {
        application.install(Sessions) {
            cookie<CookieSession>(
                    "COOKIE_NAME",
                    SessionStorageMemory()
            ) {
                @Suppress("EXPERIMENTAL_API_USAGE")
                identity { UUID.randomUUID().toString() }
                cookie.path = "/"
            }
            header<HeaderSession>(
                    "HEADER_NAME",
                    SessionStorageMemory()
            ) {
                identity { UUID.randomUUID().toString() }
            }
        }

    }

    fun installCallLogging(application: Application) {
        application.install(CallLogging) {
            level = Level.INFO
            logger = LOG
            filter(
                    predicate = { call -> call.request.path().startsWith("/") }
            )
            mdc(
                    name = "requestId",
                    provider = { call -> call.request.header("X-Request-Id") }
            )
            format { call ->
                val latency = when(val requestTime = call.attributes.getOrNull(Interceptor.REQUEST_TIME_ATTRIBUTE_KEY)) {
                    null -> -1
                    else -> System.currentTimeMillis() - requestTime
                }
                when (val status = call.response.status() ?: "404") {
                    HttpStatusCode.Found -> "ACCESS_LOG $status $latency ${call.request.toLogString()} -> ${call.response.headers[HttpHeaders.Location]}"
                    else -> "ACCESS_LOG $status $latency ${call.request.toLogString()}"
                }
            }
        }
    }

    fun installStatusPages(application: Application) {
        application.install(StatusPages) {
            exception<Throwable> { error ->
                when (error) {
                    is JsonProcessingException -> {
                        LOG.warn(call.request.uri, error)
                        call.respond(HttpStatusCode.BadRequest)
                    }
                    is DbEntityConflictException -> {
                        LOG.warn(call.request.uri, error)
                        call.respond(HttpStatusCode.Conflict)
                    }
                    is DbEntityNotFoundException -> {
                        LOG.warn(call.request.uri, error)
                        call.respond(HttpStatusCode.NotFound)
                    }
                    is CustomerClientException -> {
                        LOG.warn(call.request.uri, error)
                        call.respond(HttpStatusCode.BadRequest)
                    }
                    else -> {
                        LOG.error(call.request.uri, error)
                        call.respond(HttpStatusCode.InternalServerError)
                    }
                }
            }
        }
    }

    fun installAuthentication(application: Application) {
        application.install(Authentication) {
            basic(name = BASIC_AUTH) {
                realm = "Ktor Server - Basic Auth"
                validate { credentials ->
                    if (credentials.name == credentials.password) {
                        UserIdPrincipal(credentials.name)
                    } else {
                        null
                    }
                }
            }
        }

    }

    fun installRouting(application: Application, dependencies: Dependencies, routes: Routes, staticRoutes: StaticRoutes, locationRoutes: LocationRoutes, additionalRoutes: Route.() -> Unit, config: KtorApplicationConfig) {
        @Suppress("EXPERIMENTAL_API_USAGE")
        application.install(Locations)
        application.install(Routing) {
            if (config.enableCallTrace) {
                trace { LOG.debug(it.buildText()) }
            }
            intercept(ApplicationCallPipeline.Features, dependencies.interceptor.requestTimeInterceptor())
            routes.configure(this)
            staticRoutes.configure(this)
            locationRoutes.configure(this)
            additionalRoutes(this)
        }
    }
}