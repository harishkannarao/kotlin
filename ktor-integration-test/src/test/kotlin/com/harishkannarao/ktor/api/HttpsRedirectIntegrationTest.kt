package com.harishkannarao.ktor.api

import com.harishkannarao.ktor.AbstractBaseIntegration
import org.testng.annotations.Test

class HttpsRedirectIntegrationTest : AbstractBaseIntegration() {

    @Test
    fun `serves content on http when redirect feature is disabled`() {
        restartServerWithConfig(
                defaultConfig.copy(
                        redirectToHttps = false
                )
        )

        clients.listSnippetsApiClient()
                .get()
                .expectSuccessStatus()
    }

    @Test
    fun `redirects to https when redirect enabled`() {
        restartServerWithConfig(
                defaultConfig.copy(
                        redirectToHttps = true,
                        httpsPort = 8443
                )
        )

        val expectedRedirectUrl = "$baseUrl/snippets"
                .replace("http", "https")
                .replace("${defaultConfig.port}", "8443")

        clients.listSnippetsApiClient()
                .notFollowRedirect()
                .withXForwadedProtoHeaderAsHttp()
                .get()
                .expectPermanentlyMovedStatus()
                .expectLocationResponseHeader(expectedRedirectUrl)
    }

    @Test
    fun `redirects to https without port number when https port is 443 (default port)`() {
        restartServerWithConfig(
                defaultConfig.copy(
                        redirectToHttps = true,
                        httpsPort = 443
                )
        )

        val expectedRedirectUrl = "$baseUrl/snippets"
                .replace("http", "https")
                .replace(":${defaultConfig.port}", "")

        clients.listSnippetsApiClient()
                .notFollowRedirect()
                .withXForwadedProtoHeaderAsHttp()
                .get()
                .expectPermanentlyMovedStatus()
                .expectLocationResponseHeader(expectedRedirectUrl)
    }

    @Test
    fun `serves content on http when redirect feature is enabled and X-Forwarded-Proto header is https`() {
        restartServerWithConfig(
                defaultConfig.copy(
                        redirectToHttps = true,
                        httpsPort = 8443
                )
        )

        clients.listSnippetsApiClient()
                .withXForwadedProtoHeaderAsHttps()
                .get()
                .expectSuccessStatus()

    }
}