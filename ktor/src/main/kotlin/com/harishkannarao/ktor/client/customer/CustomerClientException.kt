package com.harishkannarao.ktor.client.customer

import java.lang.RuntimeException

class CustomerClientException(errorMessage: String) : RuntimeException(errorMessage)