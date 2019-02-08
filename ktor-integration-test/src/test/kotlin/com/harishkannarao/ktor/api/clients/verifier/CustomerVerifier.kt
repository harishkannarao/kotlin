package com.harishkannarao.ktor.api.clients.verifier

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo

class CustomerVerifier(private val customer: Customer) {

    fun get(): Customer {
        return customer
    }

    fun expectEntity(expectedResponse: Customer) {
        assertThat(customer, equalTo(expectedResponse))
    }

    data class Customer(val firstName: String, val lastName: String)
}