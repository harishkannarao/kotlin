package com.harishkannarao.ktor.client.json

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.ktor.content.TextContent
import io.ktor.http.ContentType

class ClientJsonUtil {
    val objectMapper = jacksonObjectMapper()

    fun toJson(content: Any): String {
        return objectMapper.writeValueAsString(content)
    }

    fun toJsonTextContent(customer: Any): TextContent {
        return TextContent(this.toJson(customer), contentType = ContentType.Application.Json)
    }

    inline fun <reified T> fromJson(content: String): T {
        try {
            return objectMapper.readValue(content)
        } catch (e: JsonProcessingException) {
            throw ClientJsonException(e)
        }
    }
}