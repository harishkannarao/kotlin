package com.harishkannarao.ktor

import org.testng.annotations.Test

@Test(alwaysRun = true, groups = [TestGroups.WEB_INTEGRATION_TEST])
abstract class AbstractBaseWebIntegration : AbstractBaseIntegration()