package com.harishkannarao.ktor

import com.harishkannarao.ktor.server.KtorApplicationServer

object KtorApplication {
    @JvmStatic
    fun main(args: Array<String>) {
        KtorApplicationServer().start()
    }
}

