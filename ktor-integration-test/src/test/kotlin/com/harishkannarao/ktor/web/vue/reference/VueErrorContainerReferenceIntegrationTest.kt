package com.harishkannarao.ktor.web.vue.reference

import com.harishkannarao.ktor.AbstractBaseWebIntegration
import org.testng.annotations.Test

class VueErrorContainerReferenceIntegrationTest : AbstractBaseWebIntegration() {

    @Test
    fun `display error and clears error`() {
        val webDriver = newWebDriver()

        webPages.vueErrorContainerReferencePage(webDriver)
                .get()
                .expectNoErrorMessageToBeDisplayed()
                .clickDisplayErrorButton()
                .expectErrorMessageToBe("My sample error message")
                .clickClearErrorButton()
                .expectNoErrorMessageToBeDisplayed()
    }
}