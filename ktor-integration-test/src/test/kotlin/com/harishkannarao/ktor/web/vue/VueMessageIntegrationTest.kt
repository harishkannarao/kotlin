package com.harishkannarao.ktor.web.vue

import com.harishkannarao.ktor.AbstractBaseWebIntegration
import org.testng.annotations.Test

class VueMessageIntegrationTest: AbstractBaseWebIntegration() {

    @Test
    fun `test vue message`() {
        val webDriver = newWebDriver()
        webPages.vueExampleWebPage(webDriver)
                .get()
                .expectVueMessage()
    }
}