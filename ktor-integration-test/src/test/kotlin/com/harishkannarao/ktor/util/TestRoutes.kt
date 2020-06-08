package com.harishkannarao.ktor.util

import io.ktor.application.call
import io.ktor.freemarker.FreeMarkerContent
import io.ktor.locations.KtorExperimentalLocationsAPI
import io.ktor.locations.get
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.route

object TestRoutes {
    @Suppress("EXPERIMENTAL_API_USAGE")
    fun createTestRoutes(): Route.() -> Unit = {
        route("") {
            route("vue") {
                route("error_container_reference") {
                    get<Unit> {
                        call.respond(FreeMarkerContent("/vue/vue_error_container_reference.ftl", null))
                    }
                }
            }
        }
    }
}