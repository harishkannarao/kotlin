package com.harishkannarao.ktor.module

import com.fasterxml.jackson.module.kotlin.MissingKotlinParameterException
import com.harishkannarao.ktor.config.KtorApplicationConfig
import com.harishkannarao.ktor.route.Routes
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.ContentNegotiation
import io.ktor.features.StatusPages
import io.ktor.http.HttpStatusCode
import io.ktor.jackson.jackson
import io.ktor.request.uri
import io.ktor.response.respond
import io.ktor.routing.routing
import org.slf4j.LoggerFactory

class Modules(
        private val config: KtorApplicationConfig,
        private val routes: Routes
) {

    private val logger = LoggerFactory.getLogger(Modules::class.java)

    val myModule: Application.() -> Unit = {
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
            routes.rootPath(this)
            if (config.enableSnippetsApi) {
                routes.snippetsPath(this)
            }
            routes.fileEchoPath(this)
        }
    }
}