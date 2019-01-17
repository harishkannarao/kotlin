package com.harishkannarao.ktor.web.clients

import com.gargoylesoftware.htmlunit.WebClient
import com.gargoylesoftware.htmlunit.html.DomNode
import com.gargoylesoftware.htmlunit.html.HtmlPage

@Suppress("UNCHECKED_CAST")
abstract class WebClientBase<T : WebClientBase<T>>(private val baseUrl: String, private val webClient: WebClient) {

    lateinit var htmlPage: HtmlPage

    protected fun getPage(relativePath: String): T {
        htmlPage = webClient.getPage("$baseUrl$relativePath")
        return this as T
    }

    protected fun <ET : DomNode> getElement(cssSelector: String): ET {
        return htmlPage.querySelector(cssSelector)
    }
}