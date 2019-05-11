package com.harishkannarao.ktor.web.clients

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.containsString
import org.openqa.selenium.WebDriver

class UserWebPage(baseUrl: String, webClient: WebDriver) : WebPageBase<UserWebPage>(baseUrl, webClient) {
    fun get(): UserWebPage {
        return navigateTo("/user")
    }

    fun expectNameToBe(value: String): UserWebPage {
        val name = getElement(".qa-name")
        assertThat(name.text, containsString(value))
        return this
    }

    fun expectEmailToBe(value: String) {
        val email = getElement(".qa-email")
        assertThat(email.text, containsString(value))
    }
}