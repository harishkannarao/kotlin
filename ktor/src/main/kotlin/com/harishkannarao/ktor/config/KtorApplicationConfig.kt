package com.harishkannarao.ktor.config

import java.lang.IllegalStateException

data class KtorApplicationConfig(
        val port: Int = lookupOrDefault("PORT", "port", "8080").toInt(),
        val shutdownGracePeriodInMillis: Long = 0L,
        val shutdownTimeoutInMillis: Long = 0L,
        val enableSnippetsApi: Boolean = true,
        val httpsPort: Int = lookupOrDefault("PORT", "port", "8443").toInt(),
        val thirdPartyCustomerServiceUrl: String = lookupOrDefault("CUSTOMER_SERVICE_BASE_URL", "customer.service.base.url", "http://localhost:8089"),
        val thirdPartyApiUrl: String = lookupOrDefault("THIRD_PARTY_API_URL", "thirdParty.url", "http://localhost:8089/third-party"),
        val jdbcUrl: String = lookupOrDefault("JDBC_URL", "jdbc.url", "jdbc:postgresql://localhost:25432/myuser"),
        val jdbcUserName: String = lookupOrDefault("JDBC_USERNAME", "jdbc.username", "myuser"),
        val jdbcPassword: String = lookupOrDefault("JDBC_PASSWORD", "jdbc.password", "superpassword"),
        val developmentMode: Boolean = lookupOrDefault("APP_DEVELOPMENT_MODE", "app.development.mode", "false").toBoolean(),
        val enableCallTrace: Boolean = lookupOrDefault("APP_ENABLE_CALL_TRACE", "app.enable.call.trace", "false").toBoolean(),
        val jsConfigProfile: String = lookupOrDefault("JS_CONFIG_PROFILE", "jsConfigProfile", "local")
) {
    companion object {
        @Suppress("MemberVisibilityCanBePrivate", "unused")
        fun lookupOrNull(env: String, sysProp: String): String? {
            return System.getProperty(sysProp) ?: System.getenv(env)
        }

        @Suppress("MemberVisibilityCanBePrivate", "unused")
        fun lookup(env: String, sysProp: String): String {
            return lookupOrNull(env, sysProp)
                    ?: throw IllegalStateException("Missing mandatory value for environment variable '$env' or system property '$sysProp'")
        }

        @Suppress("MemberVisibilityCanBePrivate", "unused")
        fun lookupOrDefault(env: String, sysProp: String, default: String): String {
            return lookupOrNull(env, sysProp) ?: default
        }
    }
}