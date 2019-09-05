package com.harishkannarao.ktor.module

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
import io.ktor.application.Application
import io.ktor.application.ApplicationCallPipeline
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.auth.Authentication
import io.ktor.auth.UserIdPrincipal
import io.ktor.auth.basic
import io.ktor.features.*
import io.ktor.freemarker.FreeMarker
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.jackson.jackson
import io.ktor.locations.Locations
import io.ktor.request.header
import io.ktor.request.path
import io.ktor.request.uri
import io.ktor.response.respond
import io.ktor.routing.Routing
import io.ktor.sessions.SessionStorageMemory
import io.ktor.sessions.Sessions
import io.ktor.sessions.cookie
import io.ktor.sessions.header
import io.ktor.webjars.Webjars
import org.slf4j.LoggerFactory
import org.slf4j.event.Level
import java.util.*


class Modules(
        private val config: KtorApplicationConfig,
        private val routes: Routes,
        private val staticRoutes: StaticRoutes,
        private val locationRoutes: LocationRoutes,
        private val dependencies: Dependencies
) {

    private val logger = LoggerFactory.getLogger(Modules::class.java)

    val myModule: Application.() -> Unit = {
        install(ContentNegotiation) {
            jackson {
                this.registerModule(ParameterNamesModule())
                this.registerModule(Jdk8Module())
                this.registerModule(JavaTimeModule())
                this.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
            }
        }
        install(Webjars)
        install(AutoHeadResponse)
        install(FreeMarker) {
            this.templateLoader = ClassTemplateLoader(Modules::class.java.classLoader, "templates")
            if (config.developmentMode) {
                this.setSharedVariable("javaScriptVariant", "")
                this.setSharedVariable("cssVariant", "")
            } else {
                this.setSharedVariable("javaScriptVariant", ".min")
                this.setSharedVariable("cssVariant", ".min")
            }
        }
        install(Sessions) {
            cookie<CookieSession>(
                    "COOKIE_NAME",
                    SessionStorageMemory()
            ) {
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
        if (config.redirectToHttps) {
            install(XForwardedHeaderSupport)
            install(HttpsRedirect) {
                sslPort = config.httpsPort
                permanentRedirect = true
            }
        }
        install(CallLogging) {
            level = Level.INFO
            filter(
                    predicate = { call -> call.request.path().startsWith("/") }
            )
            mdc(
                    name = "requestId",
                    provider = { call -> call.request.header("X-Request-Id") }
            )
            format { call ->
                val latency = when(val requestTime = call.attributes.getOrNull(Interceptor.REQUEST_TIME_ATTRIBUTE_KEY)) {
                    null -> 0
                    else -> System.currentTimeMillis() - requestTime
                }
                when (val status = call.response.status() ?: "Unhandled") {
                    HttpStatusCode.Found -> "$status: $latency: ${call.request.toLogString()} -> ${call.response.headers[HttpHeaders.Location]}"
                    else -> "$status: $latency: ${call.request.toLogString()}"
                }
            }
        }
        install(StatusPages) {
            exception<Throwable> { error ->
                when (error) {
                    is JsonProcessingException -> {
                        logger.warn(call.request.uri, error)
                        call.respond(HttpStatusCode.BadRequest)
                    }
                    is DbEntityConflictException -> {
                        logger.warn(call.request.uri, error)
                        call.respond(HttpStatusCode.Conflict)
                    }
                    is DbEntityNotFoundException -> {
                        logger.warn(call.request.uri, error)
                        call.respond(HttpStatusCode.NotFound)
                    }
                    is CustomerClientException -> {
                        logger.warn(call.request.uri, error)
                        call.respond(HttpStatusCode.BadRequest)
                    }
                    else -> {
                        logger.error(call.request.uri, error)
                        call.respond(HttpStatusCode.InternalServerError)
                    }
                }
            }
        }
        install(Authentication) {
            basic(name = BASIC_AUTH) {
                realm = BASIC_AUTH_REALM
                validate { credentials ->
                    if (credentials.name == credentials.password) {
                        UserIdPrincipal(credentials.name)
                    } else {
                        null
                    }
                }
            }
        }
        install(Locations)
        install(Routing) {
            if (config.developmentMode) {
                trace { logger.debug(it.buildText()) }
            }
            intercept(ApplicationCallPipeline.Features, dependencies.interceptor.requestTimeInterceptor())
            routes.rootPath(this)
            staticRoutes.staticPath(this)
            locationRoutes.locations(this)
        }
    }

    companion object {
        const val BASIC_AUTH = "my-basic-auth"
        const val BASIC_AUTH_REALM = "Ktor Server - Basic Auth"
    }
}