package com.harishkannarao.ktor.web.clients

import org.hamcrest.Matchers
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver

@Suppress("MemberVisibilityCanBePrivate")
class VueErrorContainerReferenceWebPage(baseUrl: String, webClient: WebDriver) : WebPageBase<VueErrorContainerReferenceWebPage>(baseUrl, webClient) {
    fun get(): VueErrorContainerReferenceWebPage {
        return navigateTo(PAGE_URL)
    }

    fun expectNoErrorMessageToBeDisplayed(): VueErrorContainerReferenceWebPage {
        return expectElementNotToBeDisplayed(By.className(ERROR_MESSAGE))
    }

    fun expectErrorMessageToBe(value: String): VueErrorContainerReferenceWebPage {
        return expectElementTextToMatch(By.className(ERROR_MESSAGE), Matchers.equalTo(value))
    }

    fun clickDisplayErrorButton(): VueErrorContainerReferenceWebPage {
        clickElement(By.className(DISPLAY_ERROR_MESSAGE))
        return this
    }

    fun clickClearErrorButton(): VueErrorContainerReferenceWebPage {
        clickElement(By.className(CLEAR_ERROR_MESSAGE))
        return this
    }

    companion object {
        private const val PAGE_URL = "/vue/error_container_reference"
        private const val ERROR_MESSAGE = "qa-error-message"
        private const val CLEAR_ERROR_MESSAGE = "qa-clear-error-btn"
        private const val DISPLAY_ERROR_MESSAGE = "qa-display-error-btn"
    }
}