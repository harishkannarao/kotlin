package com.harishkannarao.ktor.api.snippets

import com.fasterxml.jackson.databind.ObjectMapper
import com.harishkannarao.ktor.config.KtorApplicationConfig

class SnippetsApi(
        private val config: KtorApplicationConfig,
        private val apiObjectMapper: ObjectMapper
) {

    fun getDefaultSnippets(): String {
        val snippets = listOf(
                SnippetDto(
                        text = config.port.toString()
                )
        )
        return apiObjectMapper.writeValueAsString(snippets)
    }

    fun createSnippet(input: String): String {
        val inputJson = apiObjectMapper.readValue(input, SnippetDto::class.java)
        return apiObjectMapper.writeValueAsString(inputJson)
    }
}