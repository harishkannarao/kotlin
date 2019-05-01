package com.harishkannarao.ktor

import com.harishkannarao.ktor.config.KtorApplicationConfig
import com.harishkannarao.ktor.dependency.Dependencies
import com.harishkannarao.ktor.module.Modules
import com.harishkannarao.ktor.route.LocationRoutes
import com.harishkannarao.ktor.route.Routes
import com.harishkannarao.ktor.route.StaticRoutes
import com.harishkannarao.ktor.server.KtorApplicationServer

object KtorApplication {
    @JvmStatic
    fun main(args: Array<String>) {
        val config = KtorApplicationConfig()
        val dependencies = Dependencies(config)
        val routes = Routes(dependencies, config)
        val locationRoutes = LocationRoutes(dependencies)
        val staticRoutes = StaticRoutes()
        val modules = Modules(config, routes, staticRoutes, locationRoutes)
        KtorApplicationServer(config, modules, dependencies).start()
    }
}

