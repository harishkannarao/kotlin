package com.harishkannarao.ktor.client.json

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.type.CollectionType
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

class ClientJsonUtil {
    val objectMapper = createAndConfigureObjectMapper()

    fun toJson(content: Any): String {
        return objectMapper.writeValueAsString(content)
    }

    fun <T> asJsonObject(content: String, clazz: Class<T>): T {
        try {
            return objectMapper.readValue(content, clazz)
        } catch (e: JsonProcessingException) {
            throw ClientJsonException(e)
        }
    }

    fun <T> asJsonList(content: String, clazz: Class<T>): List<T> {
        try {
            val javaType: CollectionType = objectMapper.typeFactory
                    .constructCollectionType(List::class.java, clazz)
            return objectMapper.readValue(content, javaType)
        } catch (e: JsonProcessingException) {
            throw ClientJsonException(e)
        }
    }

    private fun createAndConfigureObjectMapper(): ObjectMapper {
        return jacksonObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    }
}