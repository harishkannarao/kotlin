package com.harishkannarao.ktor.client.customer

class CustomerApi(
        private val customerClient: CustomerClient
) {

    suspend fun getCustomerById(id: String): CustomerDto {
        return customerClient.getCustomerById(id)
    }
}