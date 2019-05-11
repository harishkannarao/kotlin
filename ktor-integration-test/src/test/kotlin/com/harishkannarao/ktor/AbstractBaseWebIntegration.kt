package com.harishkannarao.ktor

import com.harishkannarao.ktor.web.clients.factory.WebDriverFactory
import org.testng.annotations.AfterMethod
import org.testng.annotations.Test

@Test(alwaysRun = true, groups = [TestGroups.WEB_INTEGRATION_TEST])
abstract class AbstractBaseWebIntegration : AbstractBaseIntegration() {

    @AfterMethod(alwaysRun = true)
    fun closeAllWebDrivers() {
        WebDriverFactory.closeAllWebDrivers()
    }

}