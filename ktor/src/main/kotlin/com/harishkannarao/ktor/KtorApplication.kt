package com.harishkannarao.ktor

import com.harishkannarao.ktor.config.KtorApplicationConfig
import com.harishkannarao.ktor.server.KtorApplicationServer

fun main(args: Array<String>) {
    val config = KtorApplicationConfig()
    KtorApplicationServer(config).start()
}

