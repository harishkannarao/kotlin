package com.harishkannarao.ktor.route

import com.harishkannarao.ktor.dao.entity.RelationalEntity
import com.harishkannarao.ktor.dependency.Dependencies
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.locations.*
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.route

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
                    post<CreateRelationalEntity> {
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
            }
        }
    }

    @Location("") data class CreateRelationalEntity(val placeholder: String = "")
    @Location("") data class SearchRelationalEntity(val from: String, val to: String)
    @Location("{id}") data class RelationalEntityWithId(val id: String)
}