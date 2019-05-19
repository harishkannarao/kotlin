package com.harishkannarao.ktor.route

import com.harishkannarao.ktor.api.entity.JsonEntity
import com.harishkannarao.ktor.dao.entity.RelationalEntity
import com.harishkannarao.ktor.dependency.Dependencies
import io.ktor.application.call
import io.ktor.freemarker.FreeMarkerContent
import io.ktor.http.HttpStatusCode
import io.ktor.locations.*
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.route
import java.util.*

class LocationRoutes(
        private val dependencies: Dependencies
) {
    val locations: Route.() -> Unit = {
        route("") {
            route("jdbi") {
                route("relational-entity") {
                    get<SearchRelationalEntity> { searchRelationalEntity ->
                        call.respond(dependencies.relationEntityApi.getAllEntities(searchRelationalEntity.from, searchRelationalEntity.to))
                    }
                    post<Unit> {
                        val input = call.receive<RelationalEntity.Data>()
                        val createdEntity = dependencies.relationEntityApi.createEntity(input)
                        call.respond(HttpStatusCode.Created, createdEntity)
                    }
                    get<RelationalEntityWithId> { relationalEntityWithId ->
                        call.respond(dependencies.relationEntityApi.readEntity(relationalEntityWithId.id))
                    }
                    put<RelationalEntityWithId> { relationalEntityWithId ->
                        val input = call.receive<RelationalEntity.Data>()
                        dependencies.relationEntityApi.updateEntity(relationalEntityWithId.id, input)
                        call.respond(HttpStatusCode.NoContent, Unit)
                    }
                    delete<RelationalEntityWithId> { relationalEntityWithId ->
                        dependencies.relationEntityApi.deleteEntity(relationalEntityWithId.id)
                        call.respond(HttpStatusCode.NoContent, Unit)
                    }
                }
                route("json-entity") {
                    post<Unit> {
                        val input = call.receive<JsonEntity.Data>()
                        val jsonEntity = JsonEntity(UUID.randomUUID(), input)
                        dependencies.jsonEntityApi.createEntity(jsonEntity)
                        call.respond(HttpStatusCode.Created, jsonEntity)
                    }
                    get<JsonEntityWithId> { jsonEntityWithId ->
                        call.respond(dependencies.jsonEntityApi.readEntity(jsonEntityWithId.id))
                    }
                    put<JsonEntityWithId> { jsonEntityWithId ->
                        val input = call.receive<JsonEntity.Data>()
                        val jsonEntity = JsonEntity(UUID.fromString(jsonEntityWithId.id), input)
                        dependencies.jsonEntityApi.updateEntity(jsonEntity)
                        call.respond(HttpStatusCode.NoContent, Unit)
                    }
                    delete<JsonEntityWithId> { jsonEntityWithId ->
                        dependencies.jsonEntityApi.deleteEntity(jsonEntityWithId.id)
                        call.respond(HttpStatusCode.NoContent, Unit)
                    }
                }
                get<JsonEntitySearch> { jsonEntitySearch ->
                    call.respond(dependencies.jsonEntityApi.search(jsonEntitySearch.by, jsonEntitySearch.from, jsonEntitySearch.to))
                }
                get<JsonEntitySearchByTags> { jsonEntitySearchByTags ->
                    call.respond(dependencies.jsonEntityApi.searchByTags(jsonEntitySearchByTags.tags))
                }
            }
            route("react") {
                route("like-button") {
                    get<Unit> {
                        call.respond(FreeMarkerContent("/react/like_button.ftl", null))
                    }
                }
            }
            route("vue") {
                get<VueTemplate> { vueTemplate ->
                    call.respond(FreeMarkerContent("/vue/${vueTemplate.name}.ftl", null))
                }
            }
        }
    }

    @Location("") data class SearchRelationalEntity(val from: String, val to: String)
    @Location("{id}") data class RelationalEntityWithId(val id: String)
    @Location("{id}") data class JsonEntityWithId(val id: String)
    @Location("search-json-entity") data class JsonEntitySearch(val by: String, val from: String, val to: String)
    @Location("search-json-entity-by-tags") data class JsonEntitySearchByTags(val tags: String)
    @Location("{name}") data class VueTemplate(val name: String)
}