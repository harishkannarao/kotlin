package com.harishkannarao.ktor.web.clients.factory

import com.harishkannarao.ktor.web.clients.UserWebPage
import org.openqa.selenium.WebDriver

class WebPageFactory(private val baseUrl: String) {

    fun userWebPage(webDriver: WebDriver): UserWebPage {
        return UserWebPage(baseUrl, webDriver)
    }
}