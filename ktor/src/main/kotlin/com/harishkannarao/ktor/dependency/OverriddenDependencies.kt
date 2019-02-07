package com.harishkannarao.ktor.dependency

import com.harishkannarao.ktor.api.snippets.SnippetsApi

class OverriddenDependencies(
        val overriddenSnippetsApi: SnippetsApi? = null
)