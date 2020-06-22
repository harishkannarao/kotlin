package com.harishkannarao.ktor.api.entity

import com.harishkannarao.ktor.api.helper.MockitoHelper
import com.harishkannarao.ktor.dao.JsonEntityDao
import com.harishkannarao.ktor.dao.entity.JsonDbEntity
import kotlinx.coroutines.runBlocking
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import org.mockito.ArgumentCaptor
import org.mockito.Mockito.*
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test
import java.math.BigDecimal
import java.time.LocalDate
import java.util.*

class JsonEntityApiTest {
    private lateinit var mockJsonEntityDao: JsonEntityDao
    private lateinit var mockJsonEntityMapper: JsonEntityMapper
    private lateinit var subject: JsonEntityApi

    @BeforeMethod
    fun setUp() {
        mockJsonEntityDao = mock(JsonEntityDao::class.java)
        mockJsonEntityMapper = mock(JsonEntityMapper::class.java)
        subject = JsonEntityApi(mockJsonEntityDao, mockJsonEntityMapper)
    }

    @Test
    fun `readEntityAsync returns entity from dao`() = runBlocking {
        val id = UUID.randomUUID()
        val jsonDbEntity = JsonDbEntity(
                id = id,
                booleanField = false,
                dateInEpochDays = 0L,
                decimalField = BigDecimal("0.00"),
                intField = 2,
                nestedData = emptyList(),
                tags = emptyList(),
                timeStampInEpochMillis = 0L,
                username = id.toString()
        )
        `when`(mockJsonEntityDao.readEntity(id)).thenReturn(jsonDbEntity)
        val jsonEntity = JsonEntity(
                id = id,
                data = JsonEntity.Data(
                        jsonDbEntity.username,
                        LocalDate.now(),
                        jsonDbEntity.timeStampInEpochMillis,
                        jsonDbEntity.intField,
                        jsonDbEntity.booleanField,
                        jsonDbEntity.decimalField,
                        emptyList(),
                        emptyList()
                )
        )
        `when`(mockJsonEntityMapper.fromJsonDbEntity(jsonDbEntity)).thenReturn(jsonEntity)
        val result = subject.readEntityAsync(id.toString()).await()
        assertThat(result, equalTo(jsonEntity))
    }

    @Test
    fun `deleteEntityAsync deletes the entity`() = runBlocking {
        val id = UUID.randomUUID()
        val uuidCaptor: ArgumentCaptor<UUID> = ArgumentCaptor.forClass(UUID::class.java)
        doNothing().`when`(mockJsonEntityDao).deleteEntity(MockitoHelper.capture(uuidCaptor))
        subject.deleteEntityAsync(id.toString())
        assertThat(uuidCaptor.value, equalTo(id))
    }
}