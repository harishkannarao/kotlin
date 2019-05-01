package com.harishkannarao.ktor.dao

import com.harishkannarao.ktor.dao.entity.RelationalEntity
import com.harishkannarao.ktor.dao.exception.DbEntityConflictException
import com.harishkannarao.ktor.dao.exception.DbEntityNotFoundException
import com.harishkannarao.ktor.util.DateTimeUtil.toUtcZoneOffset
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.statement.StatementContext
import java.sql.ResultSet
import java.time.OffsetDateTime
import java.util.*

class RelationalEntityDao(
        private val jdbi: Jdbi
) {

    fun createEntity(entity: RelationalEntity) {
        val returnedEntity = jdbi.withHandle<RelationalEntity, Exception> { handle ->
            handle.createUpdate(INSERT_SQL)
                    .bindMap(toSqlMap(entity))
                    .executeAndReturnGeneratedKeys()
                    .map { rs: ResultSet, _: StatementContext -> fromResultSet(rs) }
                    .first()
        }
        if (returnedEntity.id.toString() != entity.id.toString()) {
            throw DbEntityConflictException("Entity already found with id: ${returnedEntity.id} for username: ${entity.data.username}")
        }
    }

    fun readEntity(id: UUID): RelationalEntity {
        return jdbi.withHandle<RelationalEntity, Exception> { handle ->
            handle.createQuery(SELECT_SQL)
                    .bind(COLUMN_ID, id)
                    .map { rs: ResultSet, _: StatementContext -> fromResultSet(rs) }
                    .findFirst()
                    .orElseThrow { throw DbEntityNotFoundException("No entity found for id: $id") }
        }
    }

    fun updateEntity(entity: RelationalEntity) {
        val updatedRows = jdbi.withHandle<Int, Exception> { handle ->
            handle.createUpdate(UPDATE_SQL)
                    .bindMap(toSqlMap(entity))
                    .execute()
        }

        if (updatedRows == 0) {
            throw DbEntityNotFoundException("No entity found for id: ${entity.id} and username: ${entity.data.username}")
        }
    }

    fun deleteEntity(id: UUID) {
        jdbi.useHandle<Exception> { handle ->
            handle.createUpdate(DELETE_SQL)
                    .bind(COLUMN_ID, id)
                    .execute()
        }
    }

    fun getAllEntities(from: OffsetDateTime, to: OffsetDateTime): List<RelationalEntity> {
        return jdbi.withHandle<List<RelationalEntity>, Exception> { handle ->
            handle.createQuery(SELECT_ALL_SQL)
                    .bind(PARAM_FROM_DATE, from)
                    .bind(PARAM_TO_DATE, to)
                    .map { rs: ResultSet, _: StatementContext -> fromResultSet(rs) }
                    .list()
        }
    }

    private fun toSqlMap(entity: RelationalEntity): Map<String, Any> {
        return mapOf(
                COLUMN_ID to entity.id,
                COLUMN_USERNAME to entity.data.username,
                COLUMN_DATE_FIELD to toUtcZoneOffset(entity.data.dateField),
                COLUMN_LONG_FIELD to entity.data.longField,
                COLUMN_INT_FIELD to entity.data.intField,
                COLUMN_DOUBLE_FIELD to entity.data.doubleField,
                COLUMN_BOOLEAN_FIELD to entity.data.booleanField,
                COLUMN_DECIMAL_FIELD to entity.data.decimalField
        )
    }

    private fun fromResultSet(rs: ResultSet): RelationalEntity {
        val id = UUID.fromString(rs.getString(COLUMN_ID))
        val username = rs.getString(COLUMN_USERNAME)
        val dateField = toUtcZoneOffset(rs.getObject(COLUMN_DATE_FIELD, OffsetDateTime::class.java))
        val longField = rs.getLong(COLUMN_LONG_FIELD)
        val intField = rs.getInt(COLUMN_INT_FIELD)
        val doubleField = rs.getDouble(COLUMN_DOUBLE_FIELD)
        val booleanField = rs.getBoolean(COLUMN_BOOLEAN_FIELD)
        val decimalField = rs.getBigDecimal(COLUMN_DECIMAL_FIELD)

        return RelationalEntity(
                id,
                RelationalEntity.Data(
                        username,
                        dateField,
                        longField,
                        intField,
                        booleanField,
                        doubleField,
                        decimalField
                )
        )
    }

    companion object {
        private const val PARAM_FROM_DATE = "fromDate"

        private const val PARAM_TO_DATE = "toDate"
        private const val COLUMN_ID = "id"
        private const val COLUMN_USERNAME = "username"
        private const val COLUMN_DATE_FIELD = "date_field"
        private const val COLUMN_LONG_FIELD = "long_field"
        private const val COLUMN_INT_FIELD = "int_field"
        private const val COLUMN_DOUBLE_FIELD = "double_field"
        private const val COLUMN_BOOLEAN_FIELD = "boolean_field"
        private const val COLUMN_DECIMAL_FIELD = "decimal_field"

        private const val INSERT_SQL = "insert into sample_table " +
                "(id, username, date_field, long_field, int_field, double_field, boolean_field, decimal_field) " +
                "values " +
                "(:$COLUMN_ID, :$COLUMN_USERNAME, :$COLUMN_DATE_FIELD, :$COLUMN_LONG_FIELD, :$COLUMN_INT_FIELD, :$COLUMN_DOUBLE_FIELD, :$COLUMN_BOOLEAN_FIELD, :$COLUMN_DECIMAL_FIELD)" +
                "ON CONFLICT (username) DO UPDATE SET username = EXCLUDED.username " +
                "RETURNING *"

        private const val SELECT_SQL = "select id, username, date_field, long_field, int_field, double_field, boolean_field, decimal_field from sample_table where id = :$COLUMN_ID"

        private const val UPDATE_SQL = "update sample_table set " +
                "date_field = :$COLUMN_DATE_FIELD, " +
                "long_field = :$COLUMN_LONG_FIELD, " +
                "int_field = :$COLUMN_INT_FIELD, " +
                "double_field = :$COLUMN_DOUBLE_FIELD, " +
                "boolean_field = :$COLUMN_BOOLEAN_FIELD, " +
                "decimal_field = :$COLUMN_DECIMAL_FIELD " +
                "where id = :$COLUMN_ID and username = :$COLUMN_USERNAME"

        private const val DELETE_SQL = "delete from sample_table where id = :$COLUMN_ID"

        private const val SELECT_ALL_SQL = "select id, username, date_field, long_field, int_field, double_field, boolean_field, decimal_field from sample_table where date_field >= :$PARAM_FROM_DATE and date_field <= :$PARAM_TO_DATE"
    }
}