package com.harishkannarao.ktor.config

data class KtorApplicationConfig(
        val port: Int = 8080,
        val shutdownGracePeriodInMillis: Long = 30000L,
        val shutdownTimeoutInSeconds: Long = 10L
)