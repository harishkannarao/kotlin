package com.harishkannarao.ktor.route

import com.harishkannarao.ktor.api.snippets.SnippetDto
import com.harishkannarao.ktor.dependency.Dependencies
import io.ktor.application.call
import io.ktor.http.ContentType
import io.ktor.http.content.PartData
import io.ktor.http.content.forEachPart
import io.ktor.http.content.streamProvider
import io.ktor.request.contentType
import io.ktor.request.receive
import io.ktor.request.receiveMultipart
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.Routing
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.route
import org.apache.commons.io.IOUtils
import java.nio.charset.StandardCharsets

class Routes(
        private val dependencies: Dependencies
) {

    val rootPath: Routing.() -> Unit = {
        get("/") {
            call.respondText("My Example Blog", ContentType.Text.Html)
        }
    }

    val snippetsPath: Routing.() -> Unit = {
        route("/snippets") {
            get {
                call.respond(dependencies.snippetsApi.getDefaultSnippets())
            }
            post {
                val input = call.receive<SnippetDto>()
                call.respond(dependencies.snippetsApi.createSnippet(input))
            }
        }
    }

    val fileEchoPath: Routing.() -> Unit = {
        route("/file-echo") {
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
                                    fileLines = IOUtils.readLines(part.streamProvider(), StandardCharsets.UTF_8)
                                }
                            }
                        }
                        part.dispose()
                    }
                }
                call.respondText("title: $title, lines: $fileLines", ContentType.Text.Plain)
            }
        }
    }
}