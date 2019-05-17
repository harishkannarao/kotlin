package com.harishkannarao.ktor.route

import io.ktor.http.content.defaultResource
import io.ktor.http.content.resources
import io.ktor.http.content.static
import io.ktor.routing.Route

class StaticRoutes {

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
            static("css") {
                resources("static/css")
            }
        }
    }
}