package com.harishkannarao.ktor.api.clients

import io.restassured.specification.RequestSpecification
import org.hamcrest.Matchers.equalTo
import org.junit.Assert.assertThat

class FileEchoApiClient(requestSpecification: RequestSpecification) : ApiClientBase<FileEchoApiClient>(requestSpecification) {

    fun post(
            requestModifier: (FileEchoRequestDto) -> FileEchoRequestDto = { it }
    ): FileEchoApiClient {
        val requestDto = requestModifier(FileEchoRequestDto.createDefault())
        requestSpecification.basePath("/file-echo")
        requestSpecification.accept("text/plain")
        requestDto.fileContent?.let {
            requestSpecification.multiPart("text_file", "sample_file.txt", it.toByteArray(Charsets.UTF_8))
        }
        requestDto.title?.let {
            requestSpecification.multiPart("title", "some_title")
        }
        return doPost()
    }

    fun expectResponseTextToBe(expectedText: String) {
        assertThat(response().asString(), equalTo(expectedText))
    }

    data class FileEchoRequestDto(
            val title: String? = null,
            val fileContent: String? = null
    ) {
        companion object {
            fun createDefault(): FileEchoRequestDto {
                return FileEchoRequestDto()
            }
        }
    }
}