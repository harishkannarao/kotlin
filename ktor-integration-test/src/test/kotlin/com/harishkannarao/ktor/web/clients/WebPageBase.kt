package com.harishkannarao.ktor.web.clients

import org.awaitility.kotlin.await
import org.hamcrest.Matcher
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import java.util.concurrent.TimeUnit

@Suppress("UNCHECKED_CAST", "MemberVisibilityCanBePrivate")
abstract class WebPageBase<T : WebPageBase<T>>(private val baseUrl: String, protected val webDriver: WebDriver) {

    protected fun navigateTo(relativePath: String): T {
        webDriver.navigate().to("$baseUrl$relativePath")
        return this as T
    }

    protected fun getElement(cssSelector: String): WebElement {
        return webDriver.findElement(By.cssSelector(cssSelector))
    }

    protected fun expectElementTextToMatch(by: By, matcher: Matcher<String?>): T {
        return expectElementTextToMatch(by, 0, matcher)
    }

    protected fun expectElementTextToMatch(by: By, index: Int, matcher: Matcher<String?>): T {
        await.atMost(2L, TimeUnit.SECONDS)
                .pollInterval(100L, TimeUnit.MILLISECONDS)
                .until({ webDriver.findElements(by).getOrNull(index)?.text }, matcher)
        return this as T
    }

    protected fun expectElementTextToMatch(supplier: () -> String?, matcher: Matcher<String?>): T {
        await.atMost(2L, TimeUnit.SECONDS)
                .pollInterval(100L, TimeUnit.MILLISECONDS)
                .until(supplier, matcher)
        return this as T
    }
}