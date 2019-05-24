package com.harishkannarao.ktor.web.clients

import org.hamcrest.Matchers.containsString
import org.openqa.selenium.By.className
import org.openqa.selenium.WebDriver

class UserWebPage(baseUrl: String, webClient: WebDriver) : WebPageBase<UserWebPage>(baseUrl, webClient) {
    fun get(): UserWebPage {
        return navigateTo("/user")
    }

    fun expectNameToBe(value: String): UserWebPage {
        expectElementTextToMatch(className("qa-name"), containsString(value))
        return this
    }

    fun expectEmailToBe(value: String): UserWebPage {
        expectElementTextToMatch(className("qa-email"), containsString(value))
        return this
    }
}