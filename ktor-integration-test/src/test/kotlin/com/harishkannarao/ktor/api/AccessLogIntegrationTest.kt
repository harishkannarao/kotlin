package com.harishkannarao.ktor.api

import com.harishkannarao.ktor.AbstractBaseApiIntegration
import com.harishkannarao.ktor.module.Modules
import com.harishkannarao.ktor.util.LogbackTestUtil
import org.testng.annotations.AfterMethod
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

class AccessLogIntegrationTest : AbstractBaseApiIntegration() {
    private val modulesLogger = LogbackTestUtil(Modules::class.java.name)

    @BeforeMethod(alwaysRun = true)
    fun setUpTestLogger() {
        modulesLogger.setUp()
    }

    @AfterMethod(alwaysRun = true)
    fun tearDownTestLogger() {
        modulesLogger.tearDown()
    }

    @Test
    fun `access logs should print http status, latency and path for successful request`() {
        clients.rootApiClient()
                .get()
                .expectSuccessStatus()

        modulesLogger.assertLogEntryUsingRegEx("""INFO ACCESS_LOG 200 OK [0-9]+ GET - /""")
    }

    @Test
    fun `access logs should print http status, latency and path for unsuccessful request`() {
        clients.staticContentClient()
                .get("/something-junk")
                .expectNotFoundStatus()

        modulesLogger.assertLogEntry("""INFO ACCESS_LOG 404 -1 GET - /something-junk""")
    }
}