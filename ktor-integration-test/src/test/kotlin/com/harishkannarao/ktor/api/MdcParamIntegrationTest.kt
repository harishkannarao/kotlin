package com.harishkannarao.ktor.api

import com.harishkannarao.ktor.AbstractBaseIntegration
import com.harishkannarao.ktor.route.Routes
import com.harishkannarao.ktor.rule.LogbackTestAppenderRule
import org.junit.Rule
import org.junit.Test
import java.util.*

class MdcParamIntegrationTest : AbstractBaseIntegration() {

    @Rule
    @JvmField
    val routesLogger = LogbackTestAppenderRule(Routes::class.java.name)

    @Test
    fun `print request id MDC param in application log`() {
        val requestId = UUID.randomUUID().toString()
        val expectedApplicationLogMessage = "[$requestId] INFO  sample log message to test MDC param"
        clients.rootApiClient()
                .withRequestIdHeader(requestId)
                .get()
                .expectSuccessStatus()

        routesLogger.assertLogEntry(expectedApplicationLogMessage)
    }

    @Test
    fun `prints empty request id MDC param in application log`() {
        val expectedApplicationLogMessage = "[] INFO  sample log message to test MDC param"
        clients.rootApiClient()
                .get()
                .expectSuccessStatus()

        routesLogger.assertLogEntry(expectedApplicationLogMessage)
    }
}