package com.harishkannarao.ktor.api.entity

import java.math.BigDecimal
import java.time.LocalDate
import java.util.*

data class JsonEntity(
        val id: UUID,
        val data: Data
) {
    data class Data(
            val username: String,
            val date: LocalDate,
            val timeStampInEpochMillis: Long,
            val intField: Int,
            val booleanField: Boolean,
            val decimalField: BigDecimal,
            val tags: List<String> = emptyList(),
            val nestedData: List<NestedData> = emptyList()
    ) {
        data class NestedData(
                val stringField: String,
                val tags: List<String> = emptyList()
        )
    }
}