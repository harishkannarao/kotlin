package com.harishkannarao.ktor.util

import java.util.*

object TestDataUtil {
    fun randomString(): String {
        return UUID.randomUUID().toString()
    }
}