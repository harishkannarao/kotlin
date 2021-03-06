package com.harishkannarao.ktor.stub.wiremock

import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.http.ContentTypeHeader
import wiremock.org.apache.http.entity.ContentType

class WireMockStub(private val wireMock: WireMock) {

    fun setUpRootPath() {
        wireMock.register(
                WireMock.get(WireMock.urlPathMatching("/"))
                        .willReturn(
                                WireMock.aResponse()
                                        .withStatus(200)
                                        .withHeader(ContentTypeHeader.KEY, ContentType.TEXT_PLAIN.mimeType)
                                        .withBody("Hello World!!!")
                        )
        )
    }

    fun setUpGetSingleCustomer(customerId: String, response: Customer, status: Int) {
        val responseJson = WireMockJson.objectMapper.writeValueAsString(response)
        wireMock.register(
                WireMock.get(WireMock.urlPathMatching("/get-single-customer"))
                        .withQueryParam("customerId", WireMock.matching(customerId))
                        .withHeader(CUSTOM_HEADER_KEY, WireMock.matching(CUSTOM_HEADER_VALUE))
                        .willReturn(
                                WireMock.aResponse()
                                        .withStatus(status)
                                        .withHeader(ContentTypeHeader.KEY, ContentType.APPLICATION_JSON.mimeType)
                                        .withBody(responseJson)
                        )
        )
    }

    fun setUpGetMultipleCustomers(name: String, response: List<Customer>, status: Int) {
        val responseJson = WireMockJson.objectMapper.writeValueAsString(response)
        wireMock.register(
                WireMock.get(WireMock.urlPathMatching("/get-multiple-customers"))
                        .withQueryParam("name", WireMock.matching(name))
                        .withHeader(CUSTOM_HEADER_KEY, WireMock.matching(CUSTOM_HEADER_VALUE))
                        .willReturn(
                                WireMock.aResponse()
                                        .withStatus(status)
                                        .withHeader(ContentTypeHeader.KEY, ContentType.APPLICATION_JSON.mimeType)
                                        .withBody(responseJson)
                        )
        )
    }

    fun setUpCreateMultipleCustomers(request: List<Customer>, response: Response, status: Int) {
        val requestJson = WireMockJson.objectMapper.writeValueAsString(request)
        val responseJson = WireMockJson.objectMapper.writeValueAsString(response)
        wireMock.register(
                WireMock.post(WireMock.urlPathMatching("/create-multiple-customers"))
                        .withHeader(CUSTOM_HEADER_KEY, WireMock.matching(CUSTOM_HEADER_VALUE))
                        .withHeader(ContentTypeHeader.KEY, WireMock.containing(ContentType.APPLICATION_JSON.mimeType))
                        .withRequestBody(WireMock.equalToJson(requestJson, true, false))
                        .willReturn(
                                WireMock.aResponse()
                                        .withStatus(status)
                                        .withHeader(ContentTypeHeader.KEY, ContentType.APPLICATION_JSON.mimeType)
                                        .withBody(responseJson)
                        )
        )
    }

    fun verifyCreateMultipleCustomers(request: List<Customer>, count: Int = 1) {
        val requestJson = WireMockJson.objectMapper.writeValueAsString(request)
        wireMock.verifyThat(
                count,
                WireMock.postRequestedFor(WireMock.urlPathMatching("/create-multiple-customers"))
                        .withHeader(CUSTOM_HEADER_KEY, WireMock.matching(CUSTOM_HEADER_VALUE))
                        .withHeader(ContentTypeHeader.KEY, WireMock.containing(ContentType.APPLICATION_JSON.mimeType))
                        .withRequestBody(WireMock.equalToJson(requestJson, true, false))
        )
    }

    fun verifyCreateSingleCustomer(request: Customer, count: Int = 1) {
        val requestJson = WireMockJson.objectMapper.writeValueAsString(request)
        wireMock.verifyThat(
                count,
                WireMock.postRequestedFor(WireMock.urlPathMatching("/create-single-customer"))
                        .withHeader(CUSTOM_HEADER_KEY, WireMock.matching(CUSTOM_HEADER_VALUE))
                        .withHeader(ContentTypeHeader.KEY, WireMock.containing(ContentType.APPLICATION_JSON.mimeType))
                        .withRequestBody(WireMock.equalToJson(requestJson, true, false))
        )
    }

    fun setUpCreateSingleCustomer(request: Customer, status: Int) {
        val requestJson = WireMockJson.objectMapper.writeValueAsString(request)
        wireMock.register(
                WireMock.post(WireMock.urlPathMatching("/create-single-customer"))
                        .withHeader(CUSTOM_HEADER_KEY, WireMock.matching(CUSTOM_HEADER_VALUE))
                        .withHeader(ContentTypeHeader.KEY, WireMock.containing(ContentType.APPLICATION_JSON.mimeType))
                        .withRequestBody(WireMock.equalToJson(requestJson, true, false))
                        .willReturn(
                                WireMock.aResponse()
                                        .withStatus(status)
                                        .withHeader(ContentTypeHeader.KEY, ContentType.APPLICATION_JSON.mimeType)
                        )
        )
    }

    fun setUpGetThirdPartyApi(status: Int) {
        wireMock.register(
                WireMock.get(WireMock.urlPathMatching("/third-party"))
                        .willReturn(
                                WireMock.aResponse()
                                        .withStatus(status)
                        )
        )
    }

    companion object {
        const val CUSTOM_HEADER_KEY = "X-Custom-Header"
        const val CUSTOM_HEADER_VALUE = "SECRET_VALUE"
    }

    data class Customer(val firstName: String, val lastName: String) {
        companion object {
            fun newCustomer(): Customer {
                return Customer("Test First Name", "Test Last Name")
            }
        }
    }

    data class Response(val message: String) {
        companion object {
            fun newResponse(): Response {
                return Response("Success")
            }
        }
    }
}