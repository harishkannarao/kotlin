package com.harishkannarao.ktor.api

import com.harishkannarao.ktor.AbstractBaseApiIntegration
import com.harishkannarao.ktor.util.LogbackTestUtil
import com.harishkannarao.ktor.server.KtorApplicationServer
import org.testng.annotations.AfterMethod
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

class ShutdownHandlerIntegrationTest : AbstractBaseApiIntegration() {

    private val ktorApplicationServerLogger = LogbackTestUtil(KtorApplicationServer::class.java.name)

    @BeforeMethod(alwaysRun = true)
    fun setUpTestLogger() {
        ktorApplicationServerLogger.setUp()
    }

    @AfterMethod(alwaysRun = true)
    fun tearDownTestLogger() {
        ktorApplicationServerLogger.tearDown()
    }

    @Test
    fun `should execute shutdown handler`() {
        restartServerWithConfig(
                defaultConfig
        )
        val expectedApplicationLogMessage = "[] INFO  Ktor Application stopped"
        ktorApplicationServerLogger.assertLogEntry(expectedApplicationLogMessage)
    }
}