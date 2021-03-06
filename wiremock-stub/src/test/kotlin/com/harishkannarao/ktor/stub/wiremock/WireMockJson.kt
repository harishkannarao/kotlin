package com.harishkannarao.ktor.stub.wiremock

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

object WireMockJson {
    val objectMapper = createObjectMapper()

    private fun createObjectMapper(): ObjectMapper {
        return jacksonObjectMapper()
    }
}