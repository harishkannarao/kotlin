package com.harishkannarao.ktor.api

import com.harishkannarao.ktor.AbstractBaseApiIntegration
import org.testng.annotations.Test

class StaticContentIntegrationTest : AbstractBaseApiIntegration() {

    @Test
    fun `servers static html content`() {
        clients.staticContentClient()
                .get("/static/html/display.html")
                .expectSuccessStatus()
                .expectHtmlContentType()
                .expectResponseTextToContain("Display Page")
    }

    @Test
    fun `serves default html content`() {
        clients.staticContentClient()
                .get("/static/html")
                .expectSuccessStatus()
                .expectHtmlContentType()
                .expectResponseTextToContain("Default html !!!")

    }

    @Test
    fun `servers static json content`() {
        clients.staticContentClient()
                .get("/static/json/sample.json")
                .expectSuccessStatus()
                .expectJsonContentType()
                .expectResponseTextToContain("sample json")
    }

    @Test
    fun `serves default json content`() {
        clients.staticContentClient()
                .get("/static/json")
                .expectSuccessStatus()
                .expectJsonContentType()
                .expectResponseTextToContain("default json")
    }

    @Test
    fun `serves HEAD request for static content`() {
        clients.staticContentClient()
                .head("/static/html")
                .expectSuccessStatus()

        clients.staticContentClient()
                .head("/static/json")
                .expectSuccessStatus()
    }
}