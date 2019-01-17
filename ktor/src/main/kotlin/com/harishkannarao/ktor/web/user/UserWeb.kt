package com.harishkannarao.ktor.web.user

import io.ktor.freemarker.FreeMarkerContent

class UserWeb {

    fun displayUser(): FreeMarkerContent {
        val user = User("user name", "user@example.com")
        return FreeMarkerContent("user.ftl", mapOf("user" to user))
    }
}