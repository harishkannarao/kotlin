package com.harishkannarao.ktor.api.clients.verifier

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo

class CustomerVerifier(private val customer: Customer) {

    fun get(): Customer {
        return customer
    }

    fun expectEntity(expectedResponse: Customer): CustomerVerifier {
        assertThat(customer, equalTo(expectedResponse))
        return this
    }

}