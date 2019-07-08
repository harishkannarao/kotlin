package com.harishkannarao.ktor.web.clients

import org.awaitility.kotlin.await
import org.hamcrest.Matcher
import org.hamcrest.Matchers.equalTo
import org.openqa.selenium.By
import org.openqa.selenium.Keys
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.interactions.Actions
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

    protected fun getElement(by: By): WebElement {
        return webDriver.findElement(by)
    }

    protected fun getElementText(by: By): String {
        return webDriver.findElement(by).text
    }

    @Suppress("SameParameterValue")
    protected fun getElementAttribute(by: By, attribute: String): String? {
        return getElement(by).getAttribute(attribute)
    }

    protected fun getInputValueAttribute(by: By): String? {
        return getElementAttribute(by, "value")
    }

    protected fun getOptionalElement(by: By): WebElement? {
        return webDriver.findElements(by).firstOrNull()
    }

    protected fun getElements(by: By): List<WebElement> {
        return webDriver.findElements(by)
    }

    protected fun clickElement(by: By, index: Int = 0): T {
        val element = getElements(by)[index]
        Actions(webDriver).moveToElement(element).click().perform()
        return this as T
    }

    protected fun enterInput(by: By, value: String): T {
        val currentValue = getInputValueAttribute(by)
        currentValue?.let {
            repeat(it.length) {
                getElement(by).sendKeys(Keys.BACK_SPACE)
            }
        }
        getElement(by).sendKeys(value)
        return this as T
    }

    protected fun isElementSelected(by: By): Boolean {
        return getElement(by).isSelected
    }

    protected fun expectElementToBeDisplayed(by: By): T {
        return expectElementDisplayStatus(by, equalTo(true))
    }

    protected fun expectElementNotToBeDisplayed(by: By): T {
        return expectElementDisplayStatus(by, equalTo(false))
    }

    protected fun expectElementDisplayStatus(by: By, matcher: Matcher<Boolean?>): T {
        await.atMost(3L, TimeUnit.SECONDS)
                .pollInterval(100L, TimeUnit.MILLISECONDS)
                .until({ getOptionalElement(by)?.isDisplayed }, matcher)
        return this as T
    }

    protected fun expectElementTextToMatch(by: By, matcher: Matcher<String?>): T {
        return expectElementTextToMatch(by, 0, matcher)
    }

    protected fun expectElementTextToMatch(by: By, index: Int, matcher: Matcher<String?>): T {
        await.atMost(3L, TimeUnit.SECONDS)
                .pollInterval(100L, TimeUnit.MILLISECONDS)
                .until({ getElements(by).getOrNull(index)?.text }, matcher)
        return this as T
    }

    protected fun expectElementTextToMatch(supplier: () -> String?, matcher: Matcher<String?>): T {
        await.atMost(3L, TimeUnit.SECONDS)
                .pollInterval(100L, TimeUnit.MILLISECONDS)
                .until(supplier, matcher)
        return this as T
    }

    protected fun <R> expectElementToMatch(
            supplier: () -> R?,
            matcher: Matcher<R?>,
            maxTimeInMillis: Long = 3000,
            pollIntervalInMillis: Long = 100,
            pollDelayInMillis: Long = 0
    ): T {
        await.atMost(maxTimeInMillis, TimeUnit.MILLISECONDS)
                .pollDelay(pollDelayInMillis, TimeUnit.MILLISECONDS)
                .pollInterval(pollIntervalInMillis, TimeUnit.MILLISECONDS)
                .until(supplier, matcher)
        return this as T
    }

    protected fun expectElementCountToMatch(by: By, matcher: Matcher<Int>): T {
        await.atMost(3L, TimeUnit.SECONDS)
                .pollInterval(100L, TimeUnit.MILLISECONDS)
                .until({ getElements(by).size }, matcher)
        return this as T
    }
}