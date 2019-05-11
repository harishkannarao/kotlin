package com.harishkannarao.ktor.web.clients

import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement

@Suppress("UNCHECKED_CAST")
abstract class WebPageBase<T : WebPageBase<T>>(private val baseUrl: String, private val webDriver: WebDriver) {

    protected fun navigateTo(relativePath: String): T {
        webDriver.navigate().to("$baseUrl$relativePath")
        return this as T
    }

    protected fun getElement(cssSelector: String): WebElement {
        return webDriver.findElement(By.cssSelector(cssSelector))
    }
}