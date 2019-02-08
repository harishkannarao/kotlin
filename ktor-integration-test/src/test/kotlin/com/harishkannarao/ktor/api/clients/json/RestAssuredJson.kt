package com.harishkannarao.ktor.api.clients.json

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

object RestAssuredJson {
    val objectMapper = createObjectMapper()

    private fun createObjectMapper(): ObjectMapper {
        return jacksonObjectMapper()
    }
}