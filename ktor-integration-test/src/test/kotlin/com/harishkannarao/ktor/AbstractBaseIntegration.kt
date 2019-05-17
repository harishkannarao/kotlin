package com.harishkannarao.ktor

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.core.WireMockConfiguration.options
import com.harishkannarao.ktor.api.clients.factory.ClientFactory
import com.harishkannarao.ktor.config.ConfigUtil
import com.harishkannarao.ktor.config.KtorApplicationConfig
import com.harishkannarao.ktor.dependency.Dependencies
import com.harishkannarao.ktor.module.Modules
import com.harishkannarao.ktor.route.LocationRoutes
import com.harishkannarao.ktor.route.Routes
import com.harishkannarao.ktor.route.StaticRoutes
import com.harishkannarao.ktor.server.KtorApplicationServer
import com.harishkannarao.ktor.stub.wiremock.WireMockStub
import com.harishkannarao.ktor.web.clients.factory.WebDriverFactory
import com.harishkannarao.ktor.web.clients.factory.WebPageFactory
import org.awaitility.kotlin.await
import org.openqa.selenium.WebDriver
import org.testng.annotations.*
import java.net.ConnectException
import java.nio.channels.ClosedChannelException
import java.util.concurrent.TimeUnit

@Test(alwaysRun = true, groups = [TestGroups.INTEGRATION_TEST])
abstract class AbstractBaseIntegration {

    @BeforeMethod(alwaysRun = true)
    fun resetWireMock() {
        wireMockClient.resetMappings()
        wireMockClient.resetRequests()
        wireMockClient.resetScenarios()
    }

    @BeforeMethod(alwaysRun = true)
    fun restartServerWithDefaultConfig() {
        if (!runningWithDefaultConfig) {
            server.stop()
            server = createAndStartServerWithConfig(defaultConfig)
            runningWithDefaultConfig = true

            waitForServerToStart()
        }
    }

    @BeforeSuite(alwaysRun = true)
    fun globalSetup() {
        WebDriverFactory.startChromeDriverService()
    }

    @AfterSuite(alwaysRun = true)
    fun globalTearDown() {
        server.stop()
        wireMockServer.stop()
        WebDriverFactory.stopChromeDriverService()
    }

    fun newWebDriver(): WebDriver {
        return WebDriverFactory.newWebDriver()
    }

    protected fun restartServerWithConfig(config: KtorApplicationConfig) {
        server.stop()
        server = createAndStartServerWithConfig(config)
        runningWithDefaultConfig = false

        waitForServerToStart()
    }

    private fun waitForServerToStart() {
        await.alias("Wait for server to start")
                .atMost(4L, TimeUnit.SECONDS)
                .pollInterval(100L, TimeUnit.MILLISECONDS)
                .ignoreExceptionsMatching { throwable: Throwable ->
                    when (throwable) {
                        is ConnectException -> true
                        is ClosedChannelException -> true
                        else -> false
                    }
                }
                .until {
                    clients.rootApiClient()
                            .withXForwadedProtoHeaderAsHttps()
                            .get()
                            .isSuccessStatus()
                }
    }

    companion object {
        private val wireMockServer = createAndStartWireMock()
        private val wireMockClient = WireMock(wireMockServer)
        val wireMockStub = WireMockStub(wireMockClient)
        private var runningWithDefaultConfig = true
        val defaultConfig = createDefaultTestConfig()
        private var server: KtorApplicationServer = createAndStartServerWithConfig(defaultConfig)
        const val baseUrl = "http://localhost:8080"
        val clients: ClientFactory = ClientFactory(baseUrl)
        val webPages = WebPageFactory(baseUrl)

        private fun createDefaultTestConfig(): KtorApplicationConfig {
            return KtorApplicationConfig()
                    .copy(
                            developmentMode = ConfigUtil.lookupValue("APP_DEVELOPMENT_MODE","app.development.mode", "true").toBoolean()
                    )
        }

        private fun createAndStartServerWithConfig(config: KtorApplicationConfig): KtorApplicationServer {
            val dependencies = Dependencies(config = config)
            val routes = Routes(dependencies, config)
            val staticRoutes = StaticRoutes()
            val locationRoutes = LocationRoutes(dependencies)
            val modules = Modules(config, routes, staticRoutes, locationRoutes)
            val localServer = KtorApplicationServer(config, modules, dependencies)
            localServer.start(wait = false)
            return localServer
        }

        private fun createAndStartWireMock(): WireMockServer {
            val wireMockServer = WireMockServer(
                    options()
                            .port(8089)
                            .maxRequestJournalEntries(100)
            )
            wireMockServer.start()
            return wireMockServer
        }
    }
}