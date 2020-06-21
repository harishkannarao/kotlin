package com.harishkannarao.ktor.dao

import com.harishkannarao.ktor.dao.entity.JsonDbEntity
import com.harishkannarao.ktor.dao.exception.DbEntityConflictException
import com.harishkannarao.ktor.dao.exception.DbEntityNotFoundException
import com.harishkannarao.ktor.dao.json.DbJsonUtil
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.statement.StatementContext
import java.math.BigDecimal
import java.sql.ResultSet
import java.util.*
import kotlin.concurrent.thread

class JsonEntityDao(
        private val jdbi: Jdbi,
        private val dbJsonUtil: DbJsonUtil
) {

    fun createEntity(entity: JsonDbEntity) {
        jdbi.useHandle<Exception> { handle ->
            val insertedRows = handle.createUpdate(INSERT_SQL)
                    .bind(COLUMN_JSONB_DOC, dbJsonUtil.toJson(entity))
                    .execute()

            if (insertedRows == 0) {
                val returnedEntity = handle.createQuery(SELECT_BY_USER_NAME_SQL)
                        .bind(COLUMN_USER_NAME, entity.username)
                        .map { rs: ResultSet, _: StatementContext ->
                            dbJsonUtil.fromJson<JsonDbEntity>(rs.getString(COLUMN_JSONB_ENTITY))
                        }
                        .findFirst()

                if (returnedEntity.isPresent) {
                    throw DbEntityConflictException("Entity already found with id: ${returnedEntity.get().id} for username: ${entity.username}")
                }
            }
        }
    }

    fun readEntity(id: UUID): JsonDbEntity {
        val foundEntity = jdbi.withHandle<Pair<JsonDbEntity, JsonDbEntity.JsonDbEntityMetaData>, Exception> { handle ->
            handle.createQuery(SELECT_BY_ID_SQL)
                    .bind(COLUMN_ID, id.toString())
                    .map { rs: ResultSet, _: StatementContext ->
                        Pair(
                                dbJsonUtil.fromJson<JsonDbEntity>(rs.getString(COLUMN_JSONB_ENTITY)),
                                dbJsonUtil.fromJson<JsonDbEntity.JsonDbEntityMetaData>(rs.getString(COLUMN_JSONB_META_DATA))
                        )
                    }
                    .findFirst()
                    .orElseThrow { throw DbEntityNotFoundException("No entity found for id: $id") }
        }
        return foundEntity.first
    }

    fun updateEntity(entity: JsonDbEntity) {
        val updatedRows = jdbi.withHandle<Int, Exception> { handle ->
            handle.createUpdate(UPDATE_SQL)
                    .bind(COLUMN_ID, entity.id.toString())
                    .bind(COLUMN_USER_NAME, entity.username)
                    .bind(COLUMN_JSONB_DOC, dbJsonUtil.toJson(entity))
                    .execute()
        }

        if (updatedRows == 0) {
            throw DbEntityNotFoundException("No entity found for id: ${entity.id} and username: ${entity.username}")
        }
    }

    fun deleteEntity(id: UUID) {
        thread(start = true) {
            jdbi.useHandle<Exception> { handle ->
                handle.createUpdate(DELETE_SQL)
                        .bind(COLUMN_ID, id.toString())
                        .execute()
            }
        }
    }

    fun allEntities(): List<JsonDbEntity> {
        return jdbi.withHandle<List<JsonDbEntity>, Exception> { handle ->
            handle.createQuery(SELECT_ALL_SQL)
                    .map { rs: ResultSet, _: StatementContext ->
                        dbJsonUtil.fromJson<JsonDbEntity>(rs.getString(COLUMN_JSONB_ENTITY))
                    }
                    .list()
        }
    }

    fun countEntities(): Int {
        return jdbi.withHandle<Int, Exception> { handle ->
            handle.createQuery(COUNT_ALL_SQL)
                    .map { rs: ResultSet, _: StatementContext ->
                        rs.getInt("entities_count")
                    }
                    .first()
        }
    }

    fun searchByTimeStamp(from: Long, to: Long): List<JsonDbEntity> {
        return jdbi.withHandle<List<JsonDbEntity>, Exception> { handle ->
            handle.createQuery(SELECT_BY_TIMESTAMP_SQL)
                    .bind(PARAM_FROM, from)
                    .bind(PARAM_TO, to)
                    .map { rs: ResultSet, _: StatementContext ->
                        dbJsonUtil.fromJson<JsonDbEntity>(rs.getString(COLUMN_JSONB_ENTITY))
                    }
                    .list()
        }
    }

    fun searchByDate(from: Long, to: Long): List<JsonDbEntity> {
        return jdbi.withHandle<List<JsonDbEntity>, Exception> { handle ->
            handle.createQuery(SELECT_BY_DATE_SQL)
                    .bind(PARAM_FROM, from)
                    .bind(PARAM_TO, to)
                    .map { rs: ResultSet, _: StatementContext ->
                        dbJsonUtil.fromJson<JsonDbEntity>(rs.getString(COLUMN_JSONB_ENTITY))
                    }
                    .list()
        }
    }

    fun searchByDecimal(from: BigDecimal, to: BigDecimal): List<JsonDbEntity> {
        return jdbi.withHandle<List<JsonDbEntity>, Exception> { handle ->
            handle.createQuery(SELECT_BY_DECIMAL_SQL)
                    .bind(PARAM_FROM, from)
                    .bind(PARAM_TO, to)
                    .map { rs: ResultSet, _: StatementContext ->
                        dbJsonUtil.fromJson<JsonDbEntity>(rs.getString(COLUMN_JSONB_ENTITY))
                    }
                    .list()
        }
    }

    fun searchByTags(tags: List<String>): List<JsonDbEntity> {
        return jdbi.withHandle<List<JsonDbEntity>, Exception> { handle ->
            handle.createQuery(SELECT_BY_TAGS_SQL)
                    .bind(PARAM_TAGS, tags.toTypedArray())
                    .map { rs: ResultSet, _: StatementContext ->
                        dbJsonUtil.fromJson<JsonDbEntity>(rs.getString(COLUMN_JSONB_ENTITY))
                    }
                    .list()
        }
    }

    companion object {
        private const val COLUMN_JSONB_DOC = "jsonb_doc"
        private const val COLUMN_JSONB_ENTITY = "jsonb_entity"
        private const val COLUMN_JSONB_META_DATA = "jsonb_meta_data"
        private const val COLUMN_ID = "id"
        private const val COLUMN_USER_NAME = "username"
        private const val PARAM_FROM = "from"
        private const val PARAM_TO = "to"
        private const val PARAM_TAGS = "tags"

        private const val INSERT_SQL = "insert into jsonb_table (jsonb_doc) values (cast(:$COLUMN_JSONB_DOC as jsonb)) ON CONFLICT ((cast(jsonb_doc->>'username' as text))) DO NOTHING"

        private const val SELECT_BY_ID_SQL = "select (jsonb_doc - 'metaData') as $COLUMN_JSONB_ENTITY, (jsonb_doc->'metaData') as $COLUMN_JSONB_META_DATA from jsonb_table where (cast(jsonb_doc->>'id' as text)) = :$COLUMN_ID"

        private const val SELECT_BY_USER_NAME_SQL = "select (jsonb_doc - 'metaData') as $COLUMN_JSONB_ENTITY, (jsonb_doc->'metaData') as $COLUMN_JSONB_META_DATA from jsonb_table where (cast(jsonb_doc->>'username' as text)) = :$COLUMN_USER_NAME"

        private const val UPDATE_SQL = "update jsonb_table set jsonb_doc = cast(:$COLUMN_JSONB_DOC as jsonb) where (cast(jsonb_doc->>'id' as text)) = :$COLUMN_ID and (cast(jsonb_doc->>'username' as text)) = :$COLUMN_USER_NAME"

        private const val DELETE_SQL = "delete from jsonb_table where (cast(jsonb_doc->>'id' as text)) = :$COLUMN_ID"

        private const val SELECT_BY_TIMESTAMP_SQL = "select (jsonb_doc - 'metaData') as $COLUMN_JSONB_ENTITY, (jsonb_doc->'metaData') as $COLUMN_JSONB_META_DATA from jsonb_table where (cast(jsonb_doc->>'timeStampInEpochMillis' as numeric)) >= :$PARAM_FROM and (cast(jsonb_doc->>'timeStampInEpochMillis' as numeric)) <= :$PARAM_TO"

        private const val SELECT_ALL_SQL = "select (jsonb_doc - 'metaData') as $COLUMN_JSONB_ENTITY from jsonb_table"

        private const val COUNT_ALL_SQL = "select count(*) as entities_count from jsonb_table"

        private const val SELECT_BY_DATE_SQL = "select (jsonb_doc - 'metaData') as $COLUMN_JSONB_ENTITY, (jsonb_doc->'metaData') as $COLUMN_JSONB_META_DATA from jsonb_table where (cast(jsonb_doc->>'dateInEpochDays' as numeric)) >= :$PARAM_FROM and (cast(jsonb_doc->>'dateInEpochDays' as numeric)) <= :$PARAM_TO"

        private const val SELECT_BY_DECIMAL_SQL = "select (jsonb_doc - 'metaData') as $COLUMN_JSONB_ENTITY, (jsonb_doc->'metaData') as $COLUMN_JSONB_META_DATA from jsonb_table where (cast(jsonb_doc->>'decimalField' as numeric)) >= :$PARAM_FROM and (cast(jsonb_doc->>'decimalField' as numeric)) <= :$PARAM_TO"

        private const val SELECT_BY_TAGS_SQL = "select (jsonb_doc - 'metaData') as $COLUMN_JSONB_ENTITY, (jsonb_doc->'metaData') as $COLUMN_JSONB_META_DATA from jsonb_table where (jsonb_doc->'metaData'->'allTags') ??| :$PARAM_TAGS"
    }
}