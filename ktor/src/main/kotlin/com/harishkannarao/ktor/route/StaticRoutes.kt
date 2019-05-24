package com.harishkannarao.ktor.route

import com.harishkannarao.ktor.config.KtorApplicationConfig
import io.ktor.http.content.defaultResource
import io.ktor.http.content.resources
import io.ktor.http.content.static
import io.ktor.routing.Route

class StaticRoutes(
        private val config: KtorApplicationConfig
) {

    val staticPath: Route.() -> Unit = {
        static("static") {
            static("html") {
                resources("static/html")
                defaultResource("static/html/defaultHtml.html")
            }
            static("json") {
                resources("static/json")
                defaultResource("static/json/defaultJson.json")
            }
            static("js") {
                resources("static/js")
            }
            static("config.js") {
                defaultResource("static/config/${config.jsConfigProfile}.js")
            }
            static("css") {
                resources("static/css")
            }
            static("image") {
                resources("static/image")
            }
        }
    }
}