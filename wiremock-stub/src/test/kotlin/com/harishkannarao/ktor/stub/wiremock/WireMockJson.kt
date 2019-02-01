package com.harishkannarao.ktor.stub.wiremock

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

object WireMockJson {
    val objectMapper = jacksonObjectMapper()
}