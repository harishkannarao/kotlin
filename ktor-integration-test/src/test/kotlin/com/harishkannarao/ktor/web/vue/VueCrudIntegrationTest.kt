package com.harishkannarao.ktor.web.vue

import com.harishkannarao.ktor.AbstractBaseWebIntegration
import com.harishkannarao.ktor.util.DateTimeUtil
import com.harishkannarao.ktor.util.TestDataUtil
import com.harishkannarao.ktor.util.TestDataUtil.currentUtcOffsetDateTime
import org.testng.annotations.Test
import java.time.ZoneOffset
import java.time.temporal.ChronoUnit

class VueCrudIntegrationTest : AbstractBaseWebIntegration() {

    @Test
    fun `Vue can perform create and delete operation`() {
        clients.jdbiJsonEntityClient()
                .get()
                .expectSuccessStatus()
                .getEntities()
                .forEach {
                    clients.jdbiJsonEntityClient()
                            .delete(it.id)
                            .expectNoContentStatus()
                }

        val firstEntity = TestDataUtil.aRandomJsonEntity()
        val secondEntity = TestDataUtil.aRandomJsonEntity()

        val webDriver = newWebDriver()

        webPages.vueCrudWebPage(webDriver)
                .get()
                .expectLoadingMessageNotToBeDisplayed()
                .expectTotalEntities(0)
                .expectTotalEntitiesInTable(0)
                .expectAddNewButton()
                .clickAddNewButton()
                .expectNewEntityFormToBeDisplayed()
                .enterEntityDetails(
                        username = firstEntity.username,
                        date = firstEntity.date,
                        offsetDateTime = DateTimeUtil.toUtcOffsetDateTime(firstEntity.timeStampInEpochMillis),
                        intField = firstEntity.intField,
                        decimalField = firstEntity.decimalField,
                        booleanField = firstEntity.booleanField,
                        tags = firstEntity.tags
                )
                .clickSaveButton()
                .expectTotalEntities(1)
                .expectTotalEntitiesInTable(1)
                .expectEntity(
                        username = firstEntity.username,
                        date = firstEntity.date,
                        offsetDateTime = DateTimeUtil.toUtcOffsetDateTime(firstEntity.timeStampInEpochMillis),
                        intField = firstEntity.intField,
                        decimalField = firstEntity.decimalField,
                        booleanField = firstEntity.booleanField,
                        tags = firstEntity.tags
                )
                .enterEntityDetails(
                        username = secondEntity.username,
                        date = secondEntity.date,
                        offsetDateTime = DateTimeUtil.toUtcOffsetDateTime(secondEntity.timeStampInEpochMillis),
                        intField = secondEntity.intField,
                        decimalField = secondEntity.decimalField,
                        booleanField = secondEntity.booleanField,
                        tags = secondEntity.tags
                )
                .clickSaveButton()
                .expectTotalEntities(2)
                .expectTotalEntitiesInTable(2)
                .expectEntity(
                        username = secondEntity.username,
                        date = secondEntity.date,
                        offsetDateTime = DateTimeUtil.toUtcOffsetDateTime(secondEntity.timeStampInEpochMillis),
                        intField = secondEntity.intField,
                        decimalField = secondEntity.decimalField,
                        booleanField = secondEntity.booleanField,
                        tags = secondEntity.tags
                )
                .clickDeleteEntityButton(firstEntity.username)
                .expectEntityNotToBeInTable(firstEntity.username)
                .expectTotalEntities(1)
                .expectTotalEntitiesInTable(1)
                .clickDeleteEntityButton(secondEntity.username)
                .expectEntityNotToBeInTable(secondEntity.username)
                .expectTotalEntities(0)
                .expectTotalEntitiesInTable(0)
    }

    @Test
    fun `renders entities created through api`() {
        clients.jdbiJsonEntityClient()
                .get()
                .expectSuccessStatus()
                .getEntities()
                .forEach {
                    clients.jdbiJsonEntityClient()
                            .delete(it.id)
                            .expectNoContentStatus()
                }

        val firstEntity = TestDataUtil.aRandomJsonEntity()

        val secondEntity = TestDataUtil.aRandomJsonEntity()

        val firstId = clients.jdbiJsonEntityClient()
                .post(firstEntity)
                .expectCreatedStatus()
                .getEntity().id

        val secondId = clients.jdbiJsonEntityClient()
                .post(secondEntity)
                .expectCreatedStatus()
                .getEntity().id

        val webDriver = newWebDriver()

        webPages.vueCrudWebPage(webDriver)
                .get()
                .expectTotalEntities(2)
                .expectTotalEntitiesInTable(2)
                .expectEntity(
                        username = firstEntity.username,
                        date = firstEntity.date,
                        offsetDateTime = DateTimeUtil.toUtcOffsetDateTime(firstEntity.timeStampInEpochMillis),
                        intField = firstEntity.intField,
                        decimalField = firstEntity.decimalField,
                        booleanField = firstEntity.booleanField,
                        tags = firstEntity.tags,
                        id = firstId
                )
                .expectEntity(
                        username = secondEntity.username,
                        date = secondEntity.date,
                        offsetDateTime = DateTimeUtil.toUtcOffsetDateTime(secondEntity.timeStampInEpochMillis),
                        intField = secondEntity.intField,
                        decimalField = secondEntity.decimalField,
                        booleanField = secondEntity.booleanField,
                        tags = secondEntity.tags,
                        id = secondId
                )
    }

    @Test
    fun `pre fill add new entity form with default values and verify the display fields`() {
        val webDriver = newWebDriver()

        val startReferenceTime = currentUtcOffsetDateTime()
        val futureReferenceTime = currentUtcOffsetDateTime().plusDays(2).truncatedTo(ChronoUnit.MILLIS)
        val futureReferenceTimeWithOffset = currentUtcOffsetDateTime().withOffsetSameInstant(ZoneOffset.MAX).truncatedTo(ChronoUnit.MILLIS)

        webPages.vueCrudWebPage(webDriver)
                .get()
                .expectLoadingMessageNotToBeDisplayed()
                .expectAddNewButton()
                .clickAddNewButton()
                .expectNewEntityFormToBeDisplayed()
                .expectIntFieldValueToBe(1)
                .expectEpochTimestampValueBetween(startReferenceTime, currentUtcOffsetDateTime())
                .expectEpochTimestampDisplayBetween(startReferenceTime, currentUtcOffsetDateTime())
                .enterTimeStamp(DateTimeUtil.toIsoTimeStampString(futureReferenceTime))
                .expectEpochTimestampDisplayToBe(futureReferenceTime)
                .enterTimeStamp(DateTimeUtil.toIsoTimeStampString(futureReferenceTimeWithOffset))
                .expectEpochTimestampDisplayToBe(futureReferenceTimeWithOffset.withOffsetSameInstant(ZoneOffset.UTC))
                .expectBooleanFieldDisplayToBeTrue()
                .unTickBooleanField()
                .expectBooleanFieldDisplayToBeFalse()
                .tickBooleanField()
                .expectBooleanFieldDisplayToBeTrue()
                .clickDoneButton()
                .expectNewEntityFormNotToBeDisplayed()
    }

    @Test
    fun `can override initial int value`() {
        val webDriver = newWebDriver()

        webPages.vueCrudWebPage(webDriver)
                .getWithDefaultIntField(6)
                .expectAddNewButton()
                .clickAddNewButton()
                .expectNewEntityFormToBeDisplayed()
                .expectIntFieldValueToBe(6)
    }

    @Test
    fun `reset form fields after save`() {
        clients.jdbiJsonEntityClient()
                .get()
                .expectSuccessStatus()
                .getEntities()
                .forEach {
                    clients.jdbiJsonEntityClient()
                            .delete(it.id)
                            .expectNoContentStatus()
                }

        val firstEntity = TestDataUtil.aRandomJsonEntity()

        val webDriver = newWebDriver()

        webPages.vueCrudWebPage(webDriver)
                .get()
                .expectAddNewButton()
                .clickAddNewButton()
                .expectNewEntityFormToBeDisplayed()
                .expectNewEntityFormFields(
                        username = "",
                        date = "",
                        intField = 1,
                        decimalField = "0.00".toBigDecimal(),
                        booleanField = true,
                        tags = emptyList()
                )
                .enterEntityDetails(
                        username = firstEntity.username,
                        date = firstEntity.date,
                        offsetDateTime = DateTimeUtil.toUtcOffsetDateTime(firstEntity.timeStampInEpochMillis),
                        intField = firstEntity.intField,
                        decimalField = firstEntity.decimalField,
                        booleanField = firstEntity.booleanField,
                        tags = firstEntity.tags
                )
                .clickSaveButton()
                .expectTotalEntities(1)
                .expectNewEntityFormFields(
                        username = "",
                        date = "",
                        intField = 1,
                        decimalField = "0.00".toBigDecimal(),
                        booleanField = true,
                        tags = emptyList()
                )
    }
}