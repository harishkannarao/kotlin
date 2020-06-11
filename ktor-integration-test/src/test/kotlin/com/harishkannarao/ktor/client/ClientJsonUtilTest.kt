package com.harishkannarao.ktor.client

import com.harishkannarao.ktor.client.json.ClientJsonException
import com.harishkannarao.ktor.client.json.ClientJsonUtil
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.hasItem
import org.testng.annotations.Test

class ClientJsonUtilTest {

    @Test
    fun `jackson kotlin data class serialization and deserialization test`() {
        val testClass1 = TestClass("123", "abc")
        val testClass2 = TestClass("456", "def")
        val jsonObject = clientJsonUtil.toJson(testClass1)

        val jsonArray = clientJsonUtil.toJson(listOf(testClass1, testClass2))

        val testClass = clientJsonUtil.asJsonObject(jsonObject, TestClass::class.java)
        assertThat(testClass, equalTo(testClass1))

        val testClasses = clientJsonUtil.asJsonList(jsonArray, TestClass::class.java)
        assertThat(testClasses.size, equalTo(2))
        assertThat(testClasses, hasItem(testClass1))
        assertThat(testClasses, hasItem(testClass2))
    }

    @Test(expectedExceptions = [ClientJsonException::class])
    fun `jackson kotlin data deserialization with malformed json array`() {
        val jsonObject = """
            {"id":"abc","name":"123"}
        """.trimIndent()
        clientJsonUtil.asJsonList(jsonObject, TestClass::class.java)
    }

    companion object {
        val clientJsonUtil = ClientJsonUtil()
    }

    data class TestClass(val id: String, val name: String)
}