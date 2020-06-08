package com.harishkannarao.ktor.client.util

import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.readText

suspend fun HttpResponse.readTextAsUTF8(): String {
    return this.readText(Charsets.UTF_8)
}