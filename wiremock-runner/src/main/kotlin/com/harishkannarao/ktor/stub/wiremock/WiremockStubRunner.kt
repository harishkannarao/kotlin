package com.harishkannarao.ktor.stub.wiremock

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.core.WireMockConfiguration.options


object WiremockStubRunner {
    @JvmStatic
    fun main(args: Array<String>) {
        val testClass = TestClass()
        val wireMockServer = WireMockServer(
                options()
                        .port(8089)
                        .asynchronousResponseEnabled(true)
                        .asynchronousResponseThreads(10)
                        .disableRequestJournal()
        )
        wireMockServer.start()
        println(testClass.getMessage())
        println("Wiremock running at http://localhost:8089")
    }
}

