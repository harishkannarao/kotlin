package com.harishkannarao.ktor.dependency

import com.harishkannarao.ktor.api.snippets.SnippetsApi
import com.harishkannarao.ktor.config.KtorApplicationConfig
import com.harishkannarao.ktor.intercept.Interceptor
import com.harishkannarao.ktor.web.user.UserWeb

class Dependencies(
        config: KtorApplicationConfig,
        overriddenDependencies: OverriddenDependencies = OverriddenDependencies()
) {
    val interceptor = Interceptor()
    val snippetsApi: SnippetsApi = overriddenDependencies.overriddenSnippetsApi ?: SnippetsApi(config)
    val userWeb: UserWeb = UserWeb()
}