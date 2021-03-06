package com.harishkannarao.ktor.api

import com.harishkannarao.ktor.AbstractBaseApiIntegration
import com.harishkannarao.ktor.util.TestDataUtil
import org.testng.annotations.Test


class SnippetsIntegrationTest : AbstractBaseApiIntegration() {

    @Test
    fun `should list default snippets as json`() {
        clients.listSnippetsApiClient()
                .get()
                .expectSuccessStatus()
                .expectSnippetsCountToBe(1)
                .expectTextToBe(0, "8080")
    }

    @Test
    fun `should create a snippet`() {
        val input = TestDataUtil.randomString()
        clients.createSnippetsApiClient()
                .post(
                        requestModifier = {
                            it.copy(
                                    text = input
                            )
                        }
                )
                .expectSuccessStatus()
                .expectTextToBe(input)
    }

    @Test
    fun `cannot create a snippet without text`() {
        clients.createSnippetsApiClient()
                .post()
                .expectBadRequestStatus()
    }

    @Test
    fun `should return not found if snippets api is disabled`() {
        restartServerWithConfig(
                defaultConfig.copy(
                        enableSnippetsApi = false
                )
        )

        clients.listSnippetsApiClient()
                .get()
                .expectNotFoundStatus()

        clients.createSnippetsApiClient()
                .post()
                .expectNotFoundStatus()

    }
}