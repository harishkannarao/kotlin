package com.harishkannarao.ktor.intercept

import io.ktor.application.ApplicationCall
import io.ktor.http.Cookie
import io.ktor.util.AttributeKey
import io.ktor.util.pipeline.PipelineContext
import java.util.*

class Interceptor {

    fun knownCookieInterceptor(cookieName: String, key: AttributeKey<String>): suspend PipelineContext<Unit, ApplicationCall>.(Unit) -> Unit {
        return {
            val knownCookie = this.context.request.cookies[cookieName]
            val value = knownCookie ?: UUID.randomUUID().toString()
            if (knownCookie == null) {
                val cookie = Cookie(cookieName, value)
                this.context.response.cookies.append(cookie)
            }
            this.context.attributes.put(key, value)
        }
    }

    fun requestTimeInterceptor(): suspend PipelineContext<Unit, ApplicationCall>.(Unit) -> Unit {
        return {
            this.context.attributes.put(REQUEST_TIME_ATTRIBUTE_KEY, System.currentTimeMillis())
        }
    }

    companion object {
        const val KNOWN_COOKIE = "KNOWN_COOKIE"
        val KNOWN_COOKIE_ATTRIBUTE_KEY = AttributeKey<String>("KNOWN_COOKIE")
        val REQUEST_TIME_ATTRIBUTE_KEY = AttributeKey<Long>("REQUEST_TIME")
    }
}