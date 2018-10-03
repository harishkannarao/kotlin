package com.harishkannarao.ktor.api

import com.harishkannarao.ktor.AbstractBaseIntegration
import org.junit.Test


class SnippetsIntegrationTest : AbstractBaseIntegration() {

    @Test
    fun `should get snippets json`() {
        clients.snippetsApiClient()
                .get()
                .expectSuccessStatus()
                .expectOkToBe(true)
    }

    @Test
    fun `should return not found if snippets api is disabled`() {
        restartServerWithConfig(
                defaultConfig.copy(
                        enableSnippetsApi = false
                )
        )

        clients.snippetsApiClient()
                .get()
                .expectNotFoundStatus()
    }
}