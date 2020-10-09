package com.harishkannarao.ktor.server

import com.harishkannarao.ktor.config.KtorApplicationConfig
import com.harishkannarao.ktor.dependency.Dependencies
import com.harishkannarao.ktor.dependency.OverriddenDependencies
import com.harishkannarao.ktor.feature.Features
import com.harishkannarao.ktor.route.LocationRoutes
import com.harishkannarao.ktor.route.Routes
import com.harishkannarao.ktor.route.StaticRoutes
import io.ktor.application.Application
import io.ktor.application.ApplicationStopped
import io.ktor.routing.*
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.*
import org.slf4j.LoggerFactory

open class KtorApplicationServer(
        private val config: KtorApplicationConfig = KtorApplicationConfig(),
        overriddenDependencies: OverriddenDependencies = OverriddenDependencies(),
        additionalRoutes: Route.() -> Unit = {}
) {

    private val logger = LoggerFactory.getLogger(KtorApplicationServer::class.java)
    private val dependencies = Dependencies(config, overriddenDependencies)
    private val routes = Routes(dependencies, config)
    private val locationRoutes = LocationRoutes(dependencies)
    private val staticRoutes = StaticRoutes(config)
    private val stoppedEventHandler: (Application) -> Unit = {
        dependencies.client.close()
        dependencies.dataSource.close()
        logger.info("Ktor Application stopped")
    }

    private val server = embeddedServer(
            factory = Netty,
            port = config.port,
            module = {
                Features.installContentNegotiation(this)
                Features.installWebJars(this)
                Features.installAutoHeadResponse(this)
                Features.installFreeMarker(this, config)
                Features.installSessions(this)
                Features.installCallLogging(this)
                Features.installStatusPages(this)
                Features.installAuthentication(this)
                Features.installRouting(this, dependencies, routes, staticRoutes, locationRoutes, additionalRoutes, config)
            }
    )

    fun start(wait: Boolean = true) {
        server.environment.monitor.subscribe(
                definition = ApplicationStopped,
                handler = stoppedEventHandler
        )
        dependencies.flyway.migrate()
        server.start(wait)
    }

    fun stop() {
        server.stop(
                config.shutdownGracePeriodInMillis,
                config.shutdownTimeoutInMillis
        )
    }
}