package com.harishkannarao.ktor.dao.entity

import com.fasterxml.jackson.annotation.JsonProperty
import java.math.BigDecimal
import java.util.*

data class JsonDbEntity(
        val id: UUID,
        val username: String,
        val dateInEpochDays: Long,
        val timeStampInEpochMillis: Long,
        val intField: Int,
        val booleanField: Boolean,
        val decimalField: BigDecimal,
        val tags: List<String> = emptyList(),
        val nestedData: List<NestedData> = emptyList()
) {

    @JsonProperty(value = "metaData", access = JsonProperty.Access.READ_ONLY)
    fun computeMetaData(): JsonDbEntityMetaData {
        return JsonDbEntityMetaData(
                allTags = nestedData.flatMap { it.tags } + tags
        )
    }

    data class NestedData(
            val stringField: String,
            val tags: List<String> = emptyList()
    )

    data class JsonDbEntityMetaData(
            val version: Int = CURRENT_VERSION,
            val allTags: List<String> = emptyList()
    ) {
        companion object {
            const val CURRENT_VERSION = 1
        }
    }
}

