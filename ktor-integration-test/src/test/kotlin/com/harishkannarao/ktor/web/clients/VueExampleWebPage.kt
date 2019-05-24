package com.harishkannarao.ktor.web.clients

import org.hamcrest.Matchers.equalTo
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver

class VueExampleWebPage(baseUrl: String, webClient: WebDriver) : WebPageBase<VueExampleWebPage>(baseUrl, webClient) {
    fun get(): VueExampleWebPage {
        return navigateTo("/vue/vue_examples")
    }

    fun expectVueMessage(): VueExampleWebPage {
        return expectElementTextToMatch(By.className("qa-vue-message"), equalTo("Hello Vue!"))
    }
}