package com.harishkannarao.ktor

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.core.WireMockConfiguration.options
import com.harishkannarao.ktor.api.clients.factory.ClientFactory
import com.harishkannarao.ktor.config.ConfigUtil
import com.harishkannarao.ktor.config.KtorApplicationConfig
import com.harishkannarao.ktor.dependency.OverriddenDependencies
import com.harishkannarao.ktor.server.KtorApplicationServer
import com.harishkannarao.ktor.stub.wiremock.WireMockStub
import com.harishkannarao.ktor.util.TestRoutes
import com.harishkannarao.ktor.web.clients.factory.WebDriverFactory
import com.harishkannarao.ktor.web.clients.factory.WebPageFactory
import io.ktor.routing.*
import org.awaitility.kotlin.await
import org.openqa.selenium.WebDriver
import org.slf4j.LoggerFactory
import org.testng.annotations.*
import java.lang.reflect.Method
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
    fun printTestNameBeforeStarting(method: Method) {
        logger.info("Starting test: ${method.name}")
    }

    @AfterMethod(alwaysRun = true)
    fun printTestNameAfterComplete(method: Method) {
        logger.info("Completing test: ${method.name}")
    }

    @BeforeMethod(alwaysRun = true)
    fun restartServerWithDefaultConfig() {
        if (!runningWithDefaultConfig) {
            server.stop()
            server = createAndStartServerWithConfig(
                    config = defaultConfig,
                    overriddenDependencies = OverriddenDependencies(),
                    additionalRoutes = TestRoutes.createTestRoutes()
            )
            runningWithDefaultConfig = true

            waitForServerToStart()
        }
    }

    @AfterMethod(alwaysRun = true)
    fun closeAllWebDrivers() {
        WebDriverFactory.closeAllWebDrivers()
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

    protected fun restartServerWithConfig(
            config: KtorApplicationConfig = KtorApplicationConfig(),
            overriddenDependencies: OverriddenDependencies = OverriddenDependencies(),
            additionalRoutes: Route.() -> Unit = TestRoutes.createTestRoutes()
    ) {
        server.stop()
        server = createAndStartServerWithConfig(config, overriddenDependencies, additionalRoutes)
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
        private val logger = LoggerFactory.getLogger(AbstractBaseIntegration::class.java)
        private val wireMockServer = createAndStartWireMock()
        private val wireMockClient = WireMock("http", "localhost", wireMockServer.port())
        val wireMockStub = WireMockStub(wireMockClient)
        private var runningWithDefaultConfig = true
        val defaultConfig = createDefaultTestConfig()
        private var server: KtorApplicationServer = createAndStartServerWithConfig(
                config = defaultConfig,
                overriddenDependencies = OverriddenDependencies(),
                additionalRoutes = TestRoutes.createTestRoutes()
        )
        const val baseUrl = "http://localhost:8080"
        val clients: ClientFactory = ClientFactory(baseUrl, defaultConfig.enableCallTrace)
        val webPages = WebPageFactory(baseUrl)

        private fun createDefaultTestConfig(): KtorApplicationConfig {
            return KtorApplicationConfig()
                    .copy(
                            developmentMode = ConfigUtil.lookupValue("APP_DEVELOPMENT_MODE", "app.development.mode", "true").toBoolean(),
                            enableCallTrace = ConfigUtil.lookupValue("APP_ENABLE_CALL_TRACE", "app.enable.call.trace", "true").toBoolean()
                    )
        }

        private fun createAndStartServerWithConfig(
                config: KtorApplicationConfig,
                overriddenDependencies: OverriddenDependencies,
                additionalRoutes: Route.() -> Unit
        ): KtorApplicationServer {
            val localServer = KtorApplicationServer(
                    config = config,
                    overriddenDependencies = overriddenDependencies,
                    additionalRoutes = additionalRoutes
            )
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