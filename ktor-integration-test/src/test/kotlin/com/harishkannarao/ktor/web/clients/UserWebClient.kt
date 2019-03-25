package com.harishkannarao.ktor.web.clients

import com.gargoylesoftware.htmlunit.WebClient
import com.gargoylesoftware.htmlunit.html.HtmlDivision
import com.gargoylesoftware.htmlunit.html.HtmlHeading2
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.containsString

class UserWebClient(baseUrl: String, webClient: WebClient) : WebClientBase<UserWebClient>(baseUrl, webClient) {
    fun get(): UserWebClient {
        return getPage("/user")
    }

    fun expectNameToBe(value: String): UserWebClient {
        val name = getElement<HtmlHeading2>(".qa-name")
        assertThat(name.asText(), containsString(value))
        return this
    }

    fun expectEmailToBe(value: String) {
        val email = getElement<HtmlDivision>(".qa-email")
        assertThat(email.asText(), containsString(value))
    }
}