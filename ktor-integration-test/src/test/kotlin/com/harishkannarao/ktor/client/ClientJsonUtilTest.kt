package com.harishkannarao.ktor.client

import com.harishkannarao.ktor.client.json.ClientJsonException
import com.harishkannarao.ktor.client.json.ClientJsonUtil
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.hasItem
import org.junit.Test

class ClientJsonUtilTest {

    @Test
    fun `jackson kotlin data class serialization and deserialization test`() {
        val testClass1 = TestClass("123", "abc")
        val testClass2 = TestClass("456", "def")
        val jsonObject = jsonUtil.toJson(testClass1)

        val jsonArray = jsonUtil.toJson(listOf(testClass1, testClass2))

        val testClass = jsonUtil.fromJson<TestClass>(jsonObject)
        assertThat(testClass, equalTo(testClass1))

        val testClasses = jsonUtil.fromJson<List<TestClass>>(jsonArray)
        assertThat(testClasses.size, equalTo(2))
        assertThat(testClasses, hasItem(testClass1))
        assertThat(testClasses, hasItem(testClass2))
    }

    @Test(expected = ClientJsonException::class)
    fun `jackson kotlin data deserialization with malformed json array`() {
        val jsonObject = """
            {"id":"abc","name":"123"}
        """.trimIndent()
        jsonUtil.fromJson<List<TestClass>>(jsonObject)
    }

    companion object {
        val jsonUtil = ClientJsonUtil()
    }

    data class TestClass(val id: String, val name: String)
}