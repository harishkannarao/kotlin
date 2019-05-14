package com.harishkannarao.ktor.module

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.harishkannarao.ktor.api.session.CookieSession
import com.harishkannarao.ktor.api.session.HeaderSession
import com.harishkannarao.ktor.config.KtorApplicationConfig
import com.harishkannarao.ktor.route.Routes
import com.harishkannarao.ktor.route.StaticRoutes
import freemarker.cache.ClassTemplateLoader
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.auth.Authentication
import io.ktor.auth.UserIdPrincipal
import io.ktor.auth.basic
import io.ktor.features.AutoHeadResponse
import io.ktor.features.CallLogging
import io.ktor.features.ContentNegotiation
import io.ktor.features.HttpsRedirect
import io.ktor.features.StatusPages
import io.ktor.features.XForwardedHeaderSupport
import io.ktor.freemarker.FreeMarker
import io.ktor.http.HttpStatusCode
import io.ktor.jackson.jackson
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
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule
import com.harishkannarao.ktor.dao.exception.DbEntityConflictException
import com.harishkannarao.ktor.dao.exception.DbEntityNotFoundException
import com.harishkannarao.ktor.route.LocationRoutes
import io.ktor.locations.Locations


class Modules(
        private val config: KtorApplicationConfig,
        private val routes: Routes,
        private val staticRoutes: StaticRoutes,
        private val locationRoutes: LocationRoutes
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
            this.setSharedVariable("reactJsVariant", config.reactJsVariant)
            if (config.useMinifiedJavaScript) {
                this.setSharedVariable("javaScriptVariant", ".min")
            } else {
                this.setSharedVariable("javaScriptVariant", "")
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