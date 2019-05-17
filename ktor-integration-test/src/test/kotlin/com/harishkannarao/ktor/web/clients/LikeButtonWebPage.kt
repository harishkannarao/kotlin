package com.harishkannarao.ktor.web.clients

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.containsString
import org.hamcrest.Matchers.equalTo
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement

class LikeButtonWebPage(baseUrl: String, webClient: WebDriver) : WebPageBase<LikeButtonWebPage>(baseUrl, webClient) {
    fun get(): LikeButtonWebPage {
        return navigateTo("/react/like-button")
    }

    fun expectLikeButton(): LikeButtonWebPage {
        val likeButton = getLikeButton()
        assertThat(likeButton.isDisplayed, equalTo(true))
        assertThat(likeButton.text, equalTo("Click to 'Like'"))
        return this
    }

    fun clickLikeButton(): LikeButtonWebPage {
        getLikeButton().click()
        return this
    }

    fun expectLikedMessage(): LikeButtonWebPage {
        val likedMessage = getLikedMessage()
        assertThat(likedMessage.isDisplayed, equalTo(true))
        assertThat(likedMessage.text, equalTo("You liked this."))
        return this
    }

    private fun getLikeButton(): WebElement {
        return getElement(".qa-like-button")
    }

    private fun getLikedMessage(): WebElement {
        return getElement(".qa-liked-message")
    }
}