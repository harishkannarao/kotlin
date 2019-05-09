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
        val nestedData: List<NestedData> = emptyList(),
        @field:JsonProperty(value = "version", access = JsonProperty.Access.WRITE_ONLY) private val internalDbVersion: Int? = null,
        @field:JsonProperty(value = "index_field_all_tags", access = JsonProperty.Access.WRITE_ONLY) private val internalAllTags: List<String> = emptyList()
) {
    @JsonProperty(value = "version", access = JsonProperty.Access.READ_ONLY)
    fun currentVersion(): Int {
        return CURRENT_VERSION
    }

    @JsonProperty(value = "index_field_all_tags", access = JsonProperty.Access.READ_ONLY)
    fun allTags(): List<String> {
        return nestedData.flatMap { it.tags } + tags
    }

    companion object {
        const val CURRENT_VERSION = 1
    }

    data class NestedData(
            val stringField: String,
            val tags: List<String> = emptyList()
    )
}

