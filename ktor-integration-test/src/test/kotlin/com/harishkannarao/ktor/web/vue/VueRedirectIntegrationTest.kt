package com.harishkannarao.ktor.web.vue

import com.harishkannarao.ktor.AbstractBaseWebIntegration
import org.testng.annotations.Test

class VueRedirectIntegrationTest: AbstractBaseWebIntegration() {

    @Test
    fun `redirect to login page for 401 authentication error`() {
        val query = "12345"
        val webDriver = newWebDriver()

        webPages.vueRedirectWebPage(webDriver)
                .get(query)
                .clickGenerate401Button()

        webPages.vueLoginWebPage(webDriver)
                .expectToBeOnLoginPage()
                .expectRedirectUrlToBe("/vue/vue_redirect?test=$query")
    }
}