package com.harishkannarao.ktor.web.clients

import org.openqa.selenium.By
import org.openqa.selenium.WebDriver

class VueRedirectWebPage(baseUrl: String, webClient: WebDriver) : WebPageBase<VueRedirectWebPage>(baseUrl, webClient) {
    fun get(testQuery: String): VueRedirectWebPage {
        return navigateTo("/vue/vue_redirect?test=$testQuery")
    }

    fun clickGenerate401Button(): VueRedirectWebPage {
        clickElement(By.className("qa-generate-401-btn"))
        return this
    }
}