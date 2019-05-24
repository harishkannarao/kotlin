package com.harishkannarao.ktor.web.clients

import org.hamcrest.Matchers.equalTo
import org.openqa.selenium.By.className
import org.openqa.selenium.WebDriver
import java.util.*

class VueCrudWebPage(baseUrl: String, webClient: WebDriver) : WebPageBase<VueCrudWebPage>(baseUrl, webClient) {
    fun get(): VueCrudWebPage {
        return navigateTo("/vue/vue_crud")
    }

    fun expectTotalEntities(count: Int): VueCrudWebPage {
        return expectElementTextToMatch(className("qa-total-entities"), equalTo(count.toString()))
    }

    fun expectEntityInTable(index: Int, id: UUID, username: String, tags: List<String>): VueCrudWebPage {
        expectElementTextToMatch(className("qa-number"), index, equalTo((index + 1).toString()))
        expectElementTextToMatch(className("qa-id"), index, equalTo(id.toString()))
        expectElementTextToMatch(className("qa-username"), index, equalTo(username))
        tags.forEachIndexed { tagIndex, tag ->
            expectElementTextToMatch(
                    {
                        webDriver
                                .findElements(className("qa-tags")).getOrNull(index)
                                ?.findElements(className("qa-tag"))?.getOrNull(tagIndex)
                                ?.text
                    },
                    equalTo(tag)
            )
        }

        return this
    }
}