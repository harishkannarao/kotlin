package com.harishkannarao.ktor.api

import com.harishkannarao.ktor.AbstractBaseApiIntegration
import com.harishkannarao.ktor.route.Routes
import com.harishkannarao.ktor.util.LogbackTestUtil
import org.testng.annotations.AfterMethod
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test
import java.util.*

class MdcParamIntegrationTest : AbstractBaseApiIntegration() {

    private val routesLogger = LogbackTestUtil(Routes::class.java.name)

    @BeforeMethod(alwaysRun = true)
    fun setUpTestLogger() {
        routesLogger.setUp()
    }

    @AfterMethod(alwaysRun = true)
    fun tearDownTestLogger() {
        routesLogger.tearDown()
    }

    @Test
    fun `print request id MDC param in application log`() {
        val requestId = UUID.randomUUID().toString()
        val expectedApplicationLogMessage = "[$requestId] INFO sample log message to test MDC param"
        clients.rootApiClient()
                .withRequestIdHeader(requestId)
                .get()
                .expectSuccessStatus()

        routesLogger.assertLogEntry(expectedApplicationLogMessage)
    }

    @Test
    fun `prints empty request id MDC param in application log`() {
        val expectedApplicationLogMessage = "[] INFO sample log message to test MDC param"
        clients.rootApiClient()
                .get()
                .expectSuccessStatus()

        routesLogger.assertLogEntry(expectedApplicationLogMessage)
    }
}