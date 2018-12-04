package com.harishkannarao.ktor.dependency

import com.harishkannarao.ktor.api.snippets.SnippetsApi
import com.harishkannarao.ktor.config.KtorApplicationConfig

class Dependencies(
        config: KtorApplicationConfig,
        overriddenSnippetsApi: SnippetsApi? = null
) {
    val snippetsApi: SnippetsApi = overriddenSnippetsApi ?: SnippetsApi(config)
}