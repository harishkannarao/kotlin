package com.harishkannarao.ktor.dependency

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.harishkannarao.ktor.api.snippets.SnippetsApi
import com.harishkannarao.ktor.config.KtorApplicationConfig

class Dependencies(
        config: KtorApplicationConfig,
        overriddenSnippetsApi: SnippetsApi? = null
) {
    private val apiObjectMapper: ObjectMapper = ObjectMapper().registerModule(KotlinModule())
    val snippetsApi: SnippetsApi = overriddenSnippetsApi ?: SnippetsApi(config, apiObjectMapper)
}