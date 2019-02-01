package com.harishkannarao.ktor.client.json

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue

class ClientJsonUtil {
    val objectMapper = jacksonObjectMapper()

    fun toJson(content: Any): String {
        return objectMapper.writeValueAsString(content)
    }

    inline fun <reified T> fromJson(content: String): T {
        try {
            return objectMapper.readValue(content)
        } catch (e: JsonProcessingException) {
            throw ClientJsonException(e)
        }
    }
}