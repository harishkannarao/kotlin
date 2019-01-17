package com.harishkannarao.ktor.web

import com.gargoylesoftware.htmlunit.WebClient
import com.gargoylesoftware.htmlunit.html.HtmlDivision
import com.gargoylesoftware.htmlunit.html.HtmlHeading2
import com.gargoylesoftware.htmlunit.html.HtmlPage
import com.harishkannarao.ktor.AbstractBaseIntegration
import org.hamcrest.Matchers
import org.junit.Assert.assertThat
import org.junit.Test

class FreeMarkerIntegrationTest : AbstractBaseIntegration() {

    @Test
    fun `displays user details`() {
        WebClient().use { webClient ->
            val htmlPage = webClient.getPage<HtmlPage>("http://localhost:8080/user")
            val name = htmlPage.querySelector<HtmlHeading2>(".qa-name")
            assertThat(name.asText(), Matchers.containsString("user name"))
            val email = htmlPage.querySelector<HtmlDivision>(".qa-email")
            assertThat(email.asText(), Matchers.containsString("user@example.com"))
        }
    }
}