package com.harishkannarao.ktor.dao.json

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue

class DbJsonUtil {
    val objectMapper = createAndConfigureObjectMapper()

    fun toJson(content: Any): String {
        return objectMapper.writeValueAsString(content)
    }

    inline fun <reified T> fromJson(content: String): T {
        try {
            return objectMapper.readValue(content)
        } catch (e: JsonProcessingException) {
            throw DbJsonException(e)
        }
    }

    private fun createAndConfigureObjectMapper(): ObjectMapper {
        return jacksonObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    }
}