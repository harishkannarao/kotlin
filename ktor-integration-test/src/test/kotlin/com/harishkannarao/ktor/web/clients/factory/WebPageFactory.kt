package com.harishkannarao.ktor.web.clients.factory

import com.harishkannarao.ktor.web.clients.UserWebPage
import com.harishkannarao.ktor.web.clients.VueCrudWebPage
import org.openqa.selenium.WebDriver

class WebPageFactory(private val baseUrl: String) {

    fun userWebPage(webDriver: WebDriver): UserWebPage {
        return UserWebPage(baseUrl, webDriver)
    }

    fun vueCrudWebPage(webDriver: WebDriver): VueCrudWebPage {
        return VueCrudWebPage(baseUrl, webDriver)
    }
}