package com.harishkannarao.ktor.api.snippets

import com.harishkannarao.ktor.config.KtorApplicationConfig

class SnippetsApi(private val config: KtorApplicationConfig) {

    fun getDefaultSnippets(): List<SnippetDto> {
        return listOf(
                SnippetDto(
                        text = config.port.toString()
                )
        )
    }

    fun createSnippet(input: SnippetDto): SnippetDto {
        return input
    }
}