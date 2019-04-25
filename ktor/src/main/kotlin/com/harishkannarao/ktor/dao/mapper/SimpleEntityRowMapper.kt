package com.harishkannarao.ktor.dao.mapper

import com.harishkannarao.ktor.api.entity.SimpleEntity
import org.jdbi.v3.core.mapper.RowMapper
import org.jdbi.v3.core.statement.StatementContext
import java.sql.ResultSet
import java.util.*

class SimpleEntityRowMapper : RowMapper<SimpleEntity> {
    override fun map(rs: ResultSet, ctx: StatementContext): SimpleEntity {
        return SimpleEntity(UUID.fromString(rs.getString("id")), rs.getString("username"))
    }
}