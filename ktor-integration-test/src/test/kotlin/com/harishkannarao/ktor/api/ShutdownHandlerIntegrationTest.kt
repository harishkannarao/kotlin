package com.harishkannarao.ktor.api

import com.harishkannarao.ktor.AbstractBaseIntegration
import com.harishkannarao.ktor.rule.LogbackTestAppenderRule
import com.harishkannarao.ktor.server.KtorApplicationServer
import org.junit.Rule
import org.junit.Test

class ShutdownHandlerIntegrationTest : AbstractBaseIntegration() {

    @Rule
    @JvmField
    val ktorApplicationServerLogger = LogbackTestAppenderRule(KtorApplicationServer::class.java.name)

    @Test
    fun `should execute shutdown handler`() {
        restartServerWithConfig(
                defaultConfig
        )
        val expectedApplicationLogMessage = "[] INFO  Ktor Application stopped"
        ktorApplicationServerLogger.assertLogEntry(expectedApplicationLogMessage)
    }
}