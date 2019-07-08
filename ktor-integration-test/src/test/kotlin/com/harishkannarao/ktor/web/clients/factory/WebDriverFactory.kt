package com.harishkannarao.ktor.web.clients.factory

import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeDriverService
import org.openqa.selenium.chrome.ChromeOptions
import java.io.File
import java.util.ArrayList
import java.util.concurrent.TimeUnit

object WebDriverFactory {

    private val chromeDriverService = createChromeDriverService()
    private val webDrivers = ArrayList<WebDriver>()

    fun startChromeDriverService() {
        if (!chromeDriverService.isRunning) {
            chromeDriverService.start()
        }
    }

    fun stopChromeDriverService() {
        if (chromeDriverService.isRunning) {
            chromeDriverService.stop()
        }
    }

    fun newWebDriver(): WebDriver {
        val webDriver = createChromeWebDriver()
        webDrivers.add(webDriver)
        return webDriver
    }

    fun closeAllWebDrivers() {
        webDrivers.forEach {
            it.close()
            it.quit()
        }
        webDrivers.clear()
    }

    private fun createChromeDriverService(): ChromeDriverService {
        val builder = ChromeDriverService.Builder()
        val chromeDriverBinary: String? = System.getProperty("chromeDriverBinary")

        if (chromeDriverBinary != null) {
            builder.usingDriverExecutable(File(chromeDriverBinary))
        }

        return builder
                .usingAnyFreePort()
                .build()
    }

    private fun createChromeWebDriver(): WebDriver {
        val webDriver = ChromeDriver(chromeDriverService, getDefaultChromeOptions())
        webDriver.manage().timeouts().pageLoadTimeout(3, TimeUnit.MINUTES)
        return webDriver
    }

    private fun getDefaultChromeOptions(): ChromeOptions {
        val chromeOptions = ChromeOptions()
        val arguments = ArrayList<String>()
        arguments.add("--allow-insecure-localhost")
        arguments.add("--start-maximized")
        arguments.add("--disable-gpu")
        arguments.add("--no-sandbox")
        val isChromeHeadlessOn = java.lang.Boolean.parseBoolean(System.getProperty("chromeHeadless", "false"))
        if (isChromeHeadlessOn) {
            arguments.add("--headless")
        }
        chromeOptions.addArguments(arguments)
        val chromeBinary: String? = System.getProperty("chromeBinary")
        if (chromeBinary != null) {
            chromeOptions.setBinary(chromeBinary)
        }

        return chromeOptions
    }
}