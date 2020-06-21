package com.harishkannarao.ktor.api.snippets

import com.harishkannarao.ktor.config.KtorApplicationConfig
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

class SnippetsApiTest {

    private lateinit var testConfig: KtorApplicationConfig
    private lateinit var subject: SnippetsApi

    @BeforeMethod
    fun setUp() {
        testConfig = KtorApplicationConfig().copy(port = 9999)
        subject = SnippetsApi(testConfig)
    }

    @Test
    fun `default snippets should return snippets`() {
        val result = subject.getDefaultSnippets()

        assertThat(result.size, equalTo(1))
        assertThat(result[0].text, equalTo("9999"))
    }

    @Test
    fun `list snippets should return input snippets`() {
        val snippet1 = SnippetDto("abc")
        val snippet2 = SnippetDto("def")
        val input = listOf(snippet1, snippet2)

        val result = subject.createSnippet(input)
        assertThat(result.size, equalTo(2))
        assertThat(result[0], equalTo(snippet1))
        assertThat(result[1], equalTo(snippet2))
    }
}