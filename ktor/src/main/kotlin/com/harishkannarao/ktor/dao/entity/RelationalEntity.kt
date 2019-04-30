package com.harishkannarao.ktor.dao.entity

import java.math.BigDecimal
import java.time.OffsetDateTime
import java.util.*

data class RelationalEntity(
        val id: UUID,
        val data: Data
) {
    data class Data(
            val username: String,
            val dateField: OffsetDateTime,
            val longField: Long,
            val intField: Int,
            val booleanField: Boolean,
            val doubleField: Double,
            val decimalField: BigDecimal
    )
}

