package com.harishkannarao.ktor.web.clients

import org.awaitility.kotlin.await
import org.hamcrest.Matcher
import org.hamcrest.Matchers.empty
import org.hamcrest.Matchers.not
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import java.util.concurrent.TimeUnit

@Suppress("UNCHECKED_CAST", "MemberVisibilityCanBePrivate")
abstract class WebPageBase<T : WebPageBase<T>>(private val baseUrl: String, private val webDriver: WebDriver) {

    protected fun navigateTo(relativePath: String): T {
        webDriver.navigate().to("$baseUrl$relativePath")
        return this as T
    }

    protected fun getElement(cssSelector: String): WebElement {
        return webDriver.findElement(By.cssSelector(cssSelector))
    }

    protected fun getElements(cssSelector: String): List<WebElement> {
        return webDriver.findElements(By.cssSelector(cssSelector))
    }

    protected fun expectElementTextToBe(cssSelector: String, matcher: Matcher<String>): T {
        waitForElement(cssSelector)
        await.alias("Verifying element text")
                .atMost(2L, TimeUnit.SECONDS)
                .pollInterval(100L, TimeUnit.MILLISECONDS)
                .until({ getElements(cssSelector).first().text }, matcher)
        return this as T
    }

    protected fun waitForElement(cssSelector: String): T {
        await.alias("Waiting for element")
                .atMost(2L, TimeUnit.SECONDS)
                .pollInterval(100L, TimeUnit.MILLISECONDS)
                .until({ getElements(cssSelector) }, not(empty()))
        return this as T
    }
}