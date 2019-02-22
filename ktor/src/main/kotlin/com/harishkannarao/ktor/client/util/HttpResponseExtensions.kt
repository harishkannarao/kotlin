package com.harishkannarao.ktor.client.util

import io.ktor.client.response.HttpResponse
import io.ktor.client.response.readText

suspend fun HttpResponse.readTextAsUTF8(): String {
    return this.readText(Charsets.UTF_8)
}