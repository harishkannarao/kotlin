package com.harishkannarao.ktor.web.react

import com.harishkannarao.ktor.AbstractBaseWebIntegration
import org.testng.annotations.Test

class LikeButtonIntegrationTest: AbstractBaseWebIntegration() {

    @Test
    fun `test react like button`() {
        val webDriver = newWebDriver()
        webPages.likeButtonWebPage(webDriver)
                .get()
                .expectLikeButton()
                .clickLikeButton()
                .expectLikedMessage()
    }
}