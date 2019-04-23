package com.harishkannarao.ktor.api

import com.harishkannarao.ktor.AbstractBaseApiIntegration
import org.testng.annotations.Test

class FileEchoIntegrationTest : AbstractBaseApiIntegration() {

    @Test
    fun `should echo title and file content`() {
        clients.fileEchoApiClient()
                .post(
                        requestModifier = {
                            it.copy(
                                    title = "hello",
                                    fileContent = "Hello\nWorld"
                            )
                        }
                )
                .expectSuccessStatus()
                .expectResponseTextToBe("title: some_title, lines: [Hello, World]")
    }

    @Test
    fun `should echo empty title and file content`() {
        clients.fileEchoApiClient()
                .post(
                        requestModifier = {
                            it.copy(
                                    title = null,
                                    fileContent = null
                            )
                        }
                )
                .expectSuccessStatus()
                .expectResponseTextToBe("title: , lines: []")
    }
}