package com.harishkannarao.ktor

import org.testng.annotations.Test

@Test(alwaysRun = true, groups = [TestGroups.API_INTEGRATION_TEST])
abstract class AbstractBaseApiIntegration : AbstractBaseIntegration()