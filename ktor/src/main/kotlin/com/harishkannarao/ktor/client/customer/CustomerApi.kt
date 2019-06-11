package com.harishkannarao.ktor.client.customer

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class CustomerApi(
        private val customerClient: CustomerClient
) {

    suspend fun getCustomerById(id: String): CustomerDto {
        return customerClient.getCustomerById(id)
    }

    suspend fun getCustomerByIds(ids: List<String>): List<CustomerDto> = coroutineScope {
        ids.map {
            async {
                customerClient.getCustomerById(it)
            }
        }.map {
            it.await()
        }
    }

    suspend fun getCustomersByName(name: String): List<CustomerDto> {
        return customerClient.getCustomersByName(name)
    }

    suspend fun createCustomer(customer: CustomerDto) {
        customerClient.createCustomer(customer)
    }

    suspend fun createCustomers(customers: List<CustomerDto>) {
        customerClient.createCustomers(customers)
    }
}