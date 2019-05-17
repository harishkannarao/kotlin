package com.harishkannarao.ktor.config

import java.lang.IllegalStateException

object ConfigUtil {

    private fun lookupEnvironmentVariable(name: String): String? {
        return System.getenv(name)
    }

    private fun lookupSystemProperty(name: String): String? {
        return System.getProperty(name)
    }

    @Suppress("MemberVisibilityCanBePrivate", "unused")
    fun lookupOptionalValue(environmentVariable: String, systemProperty: String): String? {
        return lookupEnvironmentVariable(environmentVariable) ?: lookupSystemProperty(systemProperty)
    }

    @Suppress("MemberVisibilityCanBePrivate", "unused")
    @Throws(IllegalStateException::class)
    fun lookupMandatoryValue(environmentVariable: String, systemProperty: String): String {
        return lookupOptionalValue(environmentVariable, systemProperty)
                ?: throw IllegalStateException("Missing mandatory value for environment variable '$environmentVariable' or system property '$systemProperty'")
    }

    @Suppress("MemberVisibilityCanBePrivate", "unused")
    fun lookupValue(environmentVariable: String, systemProperty: String, defaultValue: String): String {
        return lookupOptionalValue(environmentVariable, systemProperty) ?: defaultValue
    }
}