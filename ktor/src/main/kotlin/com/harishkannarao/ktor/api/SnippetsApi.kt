package com.harishkannarao.ktor.api

class SnippetsApi {

    fun getDefaultSnippets(): List<SnippetDto> {
        return listOf(
                SnippetDto(
                        text = "hello"
                )
        )
    }

    fun createSnippet(input: SnippetDto): SnippetDto {
        return input
    }
}