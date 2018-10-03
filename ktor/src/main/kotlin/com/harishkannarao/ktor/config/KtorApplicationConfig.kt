package com.harishkannarao.ktor.config

data class KtorApplicationConfig(
        val port: Int = 8080,
        val shutdownGracePeriodInMillis: Long = 0L,
        val shutdownTimeoutInSeconds: Long = 0L,
        val enableSnippetsApi: Boolean = true
)