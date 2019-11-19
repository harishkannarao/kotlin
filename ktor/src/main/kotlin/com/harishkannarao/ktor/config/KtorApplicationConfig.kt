package com.harishkannarao.ktor.config

data class KtorApplicationConfig(
        val port: Int = ConfigUtil.lookupValue("PORT", "port", "8080").toInt(),
        val shutdownGracePeriodInMillis: Long = 0L,
        val shutdownTimeoutInSeconds: Long = 0L,
        val enableSnippetsApi: Boolean = true,
        val redirectToHttps: Boolean = false,
        val httpsPort: Int = ConfigUtil.lookupValue("PORT", "port", "8443").toInt(),
        val thirdPartyCustomerServiceUrl: String = ConfigUtil.lookupValue("CUSTOMER_SERVICE_BASE_URL", "customer.service.base.url", "http://localhost:8089"),
        val jdbcUrl: String = ConfigUtil.lookupValue("JDBC_URL","jdbc.url", "jdbc:postgresql://localhost:25432/myuser"),
        val jdbcUserName: String = ConfigUtil.lookupValue("JDBC_USERNAME","jdbc.username", "myuser"),
        val jdbcPassword: String = ConfigUtil.lookupValue("JDBC_PASSWORD","jdbc.password", "superpassword"),
        val developmentMode: Boolean = ConfigUtil.lookupValue("APP_DEVELOPMENT_MODE","app.development.mode", "false").toBoolean(),
        val enableCallTrace: Boolean = ConfigUtil.lookupValue("APP_ENABLE_CALL_TRACE","app.enable.call.trace", "false").toBoolean(),
        val jsConfigProfile: String = ConfigUtil.lookupValue("JS_CONFIG_PROFILE", "jsConfigProfile", "local")
)