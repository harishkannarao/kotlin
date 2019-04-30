package com.harishkannarao.ktor.api.clients.json

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule

object RestAssuredJson {
    val objectMapper = createObjectMapper()

    private fun createObjectMapper(): ObjectMapper {
        val jacksonObjectMapper = jacksonObjectMapper()
        jacksonObjectMapper.registerModule(ParameterNamesModule())
        jacksonObjectMapper.registerModule(Jdk8Module())
        jacksonObjectMapper.registerModule(JavaTimeModule())
        jacksonObjectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
        return jacksonObjectMapper
    }
}