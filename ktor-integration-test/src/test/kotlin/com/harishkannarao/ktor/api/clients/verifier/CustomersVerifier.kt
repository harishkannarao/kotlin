package com.harishkannarao.ktor.api.clients.verifier

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo

class CustomersVerifier(private val customers: List<Customer>) {

    fun get(): List<Customer> {
        return customers
    }

    fun expectTotalCustomersToBe(count: Int): CustomersVerifier {
        assertThat(customers.size, equalTo(count))
        return this
    }

    fun expectCustomerAtIndex(index: Int, customer: Customer): CustomersVerifier {
        assertThat(customers[index], equalTo(customer))
        return this
    }

    fun expectCustomer(customer: Customer): CustomersVerifier {
        val foundCustomer = customers.find { it.firstName == customer.firstName }
        assertThat(foundCustomer, equalTo(customer))
        return this
    }

}