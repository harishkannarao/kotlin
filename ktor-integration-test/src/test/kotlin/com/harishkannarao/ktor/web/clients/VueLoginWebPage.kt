package com.harishkannarao.ktor.web.clients

import org.hamcrest.Matchers.equalTo
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver

class VueLoginWebPage(baseUrl: String, webClient: WebDriver) : WebPageBase<VueLoginWebPage>(baseUrl, webClient) {
    fun expectToBeOnLoginPage(): VueLoginWebPage {
        expectElementTextToMatch(By.id("qa-login-message"), equalTo("Sample login page"))
        return this
    }

    fun expectRedirectUrlToBe(value: String): VueLoginWebPage {
        expectElementTextToMatch(By.id("qa-redirect-url"), equalTo("$baseUrl$value"))
        return this
    }
}