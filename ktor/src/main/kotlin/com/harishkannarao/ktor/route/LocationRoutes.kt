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
import io.ktor.routing.*
import java.util.*

class LocationRoutes(
        private val dependencies: Dependencies
) {
    fun configure(routing: Routing) {
        routing.route("") {
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
                    get<Unit> {
                        call.respond(dependencies.jsonEntityApi.allEntitiesAsync().await())
                    }
                    get<JsonEntityWithId> { jsonEntityWithId ->
                        call.respond(dependencies.jsonEntityApi.readEntityAsync(jsonEntityWithId.id).await())
                    }
                    put<JsonEntityWithId> { jsonEntityWithId ->
                        val input = call.receive<JsonEntity.Data>()
                        val jsonEntity = JsonEntity(UUID.fromString(jsonEntityWithId.id), input)
                        dependencies.jsonEntityApi.updateEntity(jsonEntity)
                        call.respond(HttpStatusCode.NoContent, Unit)
                    }
                    delete<JsonEntityWithId> { jsonEntityWithId ->
                        dependencies.jsonEntityApi.deleteEntityAsync(jsonEntityWithId.id)
                        call.respond(HttpStatusCode.NoContent, Unit)
                    }
                    route("count") {
                        get<Unit> {
                            call.respond(dependencies.jsonEntityApi.countEntitiesAsync().await())
                        }
                    }
                    route("search") {
                        get<JsonEntitySearch> { jsonEntitySearch ->
                            call.respond(dependencies.jsonEntityApi.searchAsync(jsonEntitySearch.by, jsonEntitySearch.from, jsonEntitySearch.to).await())
                        }
                    }
                    route("search-by-tags") {
                        get<JsonEntitySearchByTags> { jsonEntitySearchByTags ->
                            call.respond(dependencies.jsonEntityApi.searchByTagsAsync(jsonEntitySearchByTags.tags).await())
                        }
                    }
                }
            }
            route("vue") {
                get<VueCrudTemplate> { vueCrudTemplate ->
                    call.respond(FreeMarkerContent("/vue/vue_crud.ftl", vueCrudTemplate.createModel()))
                }
                route("vue_redirect") {
                    get<Unit> {
                        call.respond(FreeMarkerContent("/vue/vue_redirect.ftl", null))
                    }
                }
                get<VueLoginTemplate> { vueLoginTemplate ->
                    call.respond(FreeMarkerContent("/vue/vue_login.ftl", vueLoginTemplate.createModel()))
                }
            }
        }
    }

    @Location("") data class SearchRelationalEntity(val from: String, val to: String)
    @Location("{id}") data class RelationalEntityWithId(val id: String)
    @Location("{id}") data class JsonEntityWithId(val id: String)
    @Location("") data class JsonEntitySearch(val by: String, val from: String, val to: String)
    @Location("") data class JsonEntitySearchByTags(val tags: String)
    @Location("vue_crud") data class VueCrudTemplate(val defaultIntValue: Int = 1) {
        fun createModel(): Map<String, Int> {
            return mapOf(
                    "defaultIntField" to defaultIntValue
            )
        }
    }
    @Location("vue_login") data class VueLoginTemplate(val redirectTo: String?) {
        fun createModel(): Map<String, Any?> {
            return mapOf(
                    "redirectUrl" to redirectTo
            )
        }
    }
}