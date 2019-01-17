package com.harishkannarao.ktor.module

import com.fasterxml.jackson.databind.JsonMappingException
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
import org.slf4j.LoggerFactory
import org.slf4j.event.Level
import java.util.*

class Modules(
        private val config: KtorApplicationConfig,
        private val routes: Routes,
        private val staticRoutes: StaticRoutes
) {

    private val logger = LoggerFactory.getLogger(Modules::class.java)

    val myModule: Application.() -> Unit = {
        install(ContentNegotiation) {
            jackson {
            }
        }
        install(AutoHeadResponse)
        install(FreeMarker) {
            templateLoader = ClassTemplateLoader(Modules::class.java.classLoader, "templates")
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
                if (error is JsonMappingException) {
                    logger.warn(call.request.uri, error)
                    call.respond(HttpStatusCode.BadRequest)
                } else {
                    logger.error(call.request.uri, error)
                    call.respond(HttpStatusCode.InternalServerError)
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
        install(Routing) {
            routes.rootPath(this)
            staticRoutes.staticPath(this)
        }
    }

    companion object {
        const val BASIC_AUTH = "my-basic-auth"
        const val BASIC_AUTH_REALM = "Ktor Server - Basic Auth"
    }
}