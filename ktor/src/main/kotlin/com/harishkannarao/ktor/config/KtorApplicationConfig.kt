package com.harishkannarao.ktor.config

data class KtorApplicationConfig(
        val port: Int = 8080,
        val shutdownGracePeriodInMillis: Long = 0L,
        val shutdownTimeoutInSeconds: Long = 0L,
        val enableSnippetsApi: Boolean = true,
        val redirectToHttps: Boolean = false,
        val httpsPort: Int = 8443,
        val thirdPartyCustomerServiceUrl: String = "http://localhost:8089",
        val jdbcUrl: String = "jdbc:postgresql://localhost:25432/myuser",
        val jdbcUserName: String = "myuser",
        val jdbcPassword: String = "superpassword"
)