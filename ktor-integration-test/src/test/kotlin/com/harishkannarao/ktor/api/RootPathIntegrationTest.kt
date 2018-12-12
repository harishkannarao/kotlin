package com.harishkannarao.ktor.api

import com.harishkannarao.ktor.AbstractBaseIntegration
import com.harishkannarao.ktor.route.Routes
import org.junit.Test
import com.harishkannarao.ktor.rule.LogbackTestAppenderRule
import org.junit.Rule


class RootPathIntegrationTest : AbstractBaseIntegration() {

    @Rule
    val testAppenderRule = LogbackTestAppenderRule(Routes::class.java.name)

    @Test
    fun `returns the text from root path`() {
        clients.rootApiClient()
                .get()
                .expectSuccessStatus()
                .expectResponseTextToBe("My Example Blog")

//        testAppenderRule.assertLogEntry("erwrw")
    }
}