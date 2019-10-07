package com.harishkannarao.ktor.web.clients.factory

import com.harishkannarao.ktor.web.clients.*
import org.openqa.selenium.WebDriver

class WebPageFactory(private val baseUrl: String) {

    fun userWebPage(webDriver: WebDriver): UserWebPage {
        return UserWebPage(baseUrl, webDriver)
    }

    fun vueCrudWebPage(webDriver: WebDriver): VueCrudWebPage {
        return VueCrudWebPage(baseUrl, webDriver)
    }

    fun vueRedirectWebPage(webDriver: WebDriver): VueRedirectWebPage {
        return VueRedirectWebPage(baseUrl, webDriver)
    }

    fun vueLoginWebPage(webDriver: WebDriver): VueLoginWebPage {
        return VueLoginWebPage(baseUrl, webDriver)
    }

    fun vueErrorContainerReferencePage(webDriver: WebDriver): VueErrorContainerReferenceWebPage {
        return VueErrorContainerReferenceWebPage(baseUrl, webDriver)
    }
}