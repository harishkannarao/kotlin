package com.harishkannarao.ktor.web.clients

import com.harishkannarao.ktor.util.DateTimeUtil
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*
import org.openqa.selenium.By.className
import org.openqa.selenium.WebDriver
import java.math.BigDecimal
import java.time.LocalDate
import java.time.OffsetDateTime
import java.util.*

@Suppress("MemberVisibilityCanBePrivate")
class VueCrudWebPage(baseUrl: String, webClient: WebDriver) : WebPageBase<VueCrudWebPage>(baseUrl, webClient) {
    fun get(): VueCrudWebPage {
        return navigateTo(PAGE_URL)
    }

    fun getWithDefaultIntField(value: Int): VueCrudWebPage {
        return navigateTo("$PAGE_URL?defaultIntValue=$value")
    }

    fun expectTotalEntities(count: Int): VueCrudWebPage {
        return expectElementTextToMatch(className(TOTAL_ENTITIES), equalTo(count.toString()))
    }

    fun expectTotalEntitiesInServer(count: Int): VueCrudWebPage {
        return expectElementTextToMatch(className(TOTAL_SERVER_ENTITIES), equalTo(count.toString()))
    }

    fun expectAutoRefreshCheckboxToBeTicked(): VueCrudWebPage {
        assertThat(isElementSelected(className(AUTO_REFRESH_SERVER_COUNT)), equalTo(true))
        return this
    }

    fun expectAutoRefreshCheckboxToBeUnTicked(): VueCrudWebPage {
        assertThat(isElementSelected(className(AUTO_REFRESH_SERVER_COUNT)), equalTo(false))
        return this
    }

    fun tickAutoRefreshCheckbox(): VueCrudWebPage {
        if (!isElementSelected(className(AUTO_REFRESH_SERVER_COUNT))) {
            clickElement(className(AUTO_REFRESH_SERVER_COUNT))
        }
        return this
    }

    fun unTickAutoRefreshCheckbox(): VueCrudWebPage {
        if (isElementSelected(className(AUTO_REFRESH_SERVER_COUNT))) {
            clickElement(className(AUTO_REFRESH_SERVER_COUNT))
        }
        return this
    }

    fun expectTotalEntitiesInServerToRemainAt(count: Int): VueCrudWebPage {
        return expectElementToMatch(
                {
                    getElements(className(TOTAL_SERVER_ENTITIES)).firstOrNull()?.text
                },
                equalTo(count.toString()),
                pollDelayInMillis = 700
        )
    }

    fun expectTotalEntitiesInTable(count: Int): VueCrudWebPage {
        expectElementCountToMatch(className(ID), equalTo(count))
        return this
    }

    fun expectAddNewButton(): VueCrudWebPage {
        return expectElementToBeDisplayed(className(ADD_NEW_BUTTON))
    }

    fun clickAddNewButton(): VueCrudWebPage {
        clickElement(className(ADD_NEW_BUTTON))
        return this
    }

    fun clickDoneButton(): VueCrudWebPage {
        clickElement(className(DONE_BUTTON))
        return this
    }

    fun expectLoadingMessageNotToBeDisplayed(): VueCrudWebPage {
        return expectElementCountToMatch(className(LOADING_MESSAGE), equalTo(0))
    }

    fun expectRefreshingEntitiesMessageNotToBeDisplayed(): VueCrudWebPage {
        return expectElementCountToMatch(className(REFRESHING_ENTITIES_MESSAGE), equalTo(0))
    }

    fun expectNewEntityFormToBeDisplayed(): VueCrudWebPage {
        expectElementToBeDisplayed(className(NEW_ENTITY_FORM))
        return this
    }

    fun expectNewEntityFormNotToBeDisplayed() {
        expectElementNotToBeDisplayed(className(NEW_ENTITY_FORM))
    }

    fun expectEpochTimestampValueBetween(startReferenceTime: OffsetDateTime, endReferenceTime: OffsetDateTime): VueCrudWebPage {
        val valueOnTheForm = getElementAttribute(className(NEW_ENTITY_TIMESTAMP_FIELD), "value")!!
        val value = DateTimeUtil.toUtcZoneOffset(OffsetDateTime.parse(valueOnTheForm))
        val isAfterOrEqualsStartTime = (value.isAfter(startReferenceTime) || value.isEqual(startReferenceTime))
        val isBeforeOrEqualsEndTime = (value.isBefore(endReferenceTime) || value.isEqual(endReferenceTime))
        assertThat(isAfterOrEqualsStartTime, equalTo(true))
        assertThat(isBeforeOrEqualsEndTime, equalTo(true))
        return this
    }

    fun expectEpochTimestampDisplayBetween(startReferenceTime: OffsetDateTime, endReferenceTime: OffsetDateTime): VueCrudWebPage {
        val valueDisplayed = getElementText(className(NEW_ENTITY_TIMESTAMP_ISO_PRINT))
        val value = DateTimeUtil.toUtcZoneOffset(OffsetDateTime.parse(valueDisplayed))
        val isAfterOrEqualsStartTime = (value.isAfter(startReferenceTime) || value.isEqual(startReferenceTime))
        val isBeforeOrEqualsEndTime = (value.isBefore(endReferenceTime) || value.isEqual(endReferenceTime))
        assertThat(isAfterOrEqualsStartTime, equalTo(true))
        assertThat(isBeforeOrEqualsEndTime, equalTo(true))
        return this
    }

    fun expectNewEntityFormFields(
            username: String,
            date: String,
            intField: Int,
            decimalField: BigDecimal,
            booleanField: Boolean,
            tags: List<String>
    ): VueCrudWebPage {
        expectUsernameFieldValueToBe(username)
        expectDateFieldValueToBe(date)
        expectIntFieldValueToBe(intField)
        expectDecimalFieldValueToBe(decimalField)
        expectBooleanCheckBoxValueToBe(booleanField)
        expectTagFieldValueToBe(tags)
        return this
    }

    fun expectUsernameFieldValueToBe(value: String): VueCrudWebPage {
        assertThat(getElementAttribute(className(NEW_ENTITY_USERNAME_FIELD), "value"), equalTo(value))
        return this
    }

    fun expectDateFieldValueToBe(value: String): VueCrudWebPage {
        assertThat(getElementAttribute(className(NEW_ENTITY_DATE_FIELD), "value"), equalTo(value))
        return this
    }

    fun expectDecimalFieldValueToBe(value: BigDecimal): VueCrudWebPage {
        assertThat(getElementAttribute(className(NEW_ENTITY_DECIMAL_FIELD), "value"), equalTo(value.toString()))
        return this
    }

    fun expectBooleanCheckBoxValueToBe(value: Boolean): VueCrudWebPage {
        if (value) {
            assertThat(isElementSelected(className(NEW_ENTITY_BOOLEAN_FIELD)), equalTo(true))
        } else {
            assertThat(isElementSelected(className(NEW_ENTITY_BOOLEAN_FIELD)), equalTo(false))
        }
        return this
    }

    fun expectIntFieldValueToBe(value: Int): VueCrudWebPage {
        assertThat(getElementAttribute(className(NEW_ENTITY_INT_FIELD), "value"), equalTo(value.toString()))
        return this
    }

    fun expectTagFieldValueToBe(value: List<String>): VueCrudWebPage {
        assertThat(getElementAttribute(className(NEW_ENTITY_TAGS), "value"), equalTo(value.joinToString(",")))
        return this
    }

    fun enterEntityDetails(username: String, date: LocalDate, offsetDateTime: OffsetDateTime, intField: Int, decimalField: BigDecimal, booleanField: Boolean, tags: List<String>): VueCrudWebPage {
        enterUsername(username)
        enterDate(DateTimeUtil.toIsoLocalDateString(date))
        enterTimeStamp(DateTimeUtil.toIsoTimeStampString(offsetDateTime))
        enterIntField(intField.toString())
        enterDecimalField(decimalField.toString())
        if (booleanField) {
            tickBooleanField()
        } else {
            unTickBooleanField()
        }
        enterTags(tags.joinToString(","))
        return this
    }

    fun enterUsername(username: String): VueCrudWebPage {
        enterInput(className(NEW_ENTITY_USERNAME_FIELD), username)
        return this
    }

    fun enterDate(isoDateString: String): VueCrudWebPage {
        enterInput(className(NEW_ENTITY_DATE_FIELD), isoDateString)
        return this
    }

    fun enterIntField(intField: String): VueCrudWebPage {
        enterInput(className(NEW_ENTITY_INT_FIELD), intField)
        return this
    }

    fun enterDecimalField(decimalField: String): VueCrudWebPage {
        enterInput(className(NEW_ENTITY_DECIMAL_FIELD), decimalField)
        return this
    }

    fun enterTags(commaSeperatedTags: String): VueCrudWebPage {
        enterInput(className(NEW_ENTITY_TAGS), commaSeperatedTags)
        return this
    }

    fun enterTimeStamp(isoTimeStampString: String): VueCrudWebPage {
        enterInput(className(NEW_ENTITY_TIMESTAMP_FIELD), isoTimeStampString)
        return this
    }

    fun expectEpochTimestampDisplayToBe(futureReferenceTime: OffsetDateTime): VueCrudWebPage {
        expectElementToMatch(
                {
                    getOptionalElement(className(NEW_ENTITY_TIMESTAMP_ISO_PRINT))?.let { DateTimeUtil.toUtcZoneOffset(OffsetDateTime.parse(it.text)) }
                },
                equalTo(futureReferenceTime)
        )
        return this
    }

    fun tickBooleanField(): VueCrudWebPage {
        if (!isElementSelected(className(NEW_ENTITY_BOOLEAN_FIELD))) {
            clickElement(className(NEW_ENTITY_BOOLEAN_FIELD))
        }
        return this
    }

    fun expectBooleanFieldDisplayToBeTrue(): VueCrudWebPage {
        expectElementTextToMatch(className(NEW_ENTITY_BOOLEAN_FIELD_DISPLAY), equalTo("true"))
        return this
    }

    fun unTickBooleanField(): VueCrudWebPage {
        if (isElementSelected(className(NEW_ENTITY_BOOLEAN_FIELD))) {
            clickElement(className(NEW_ENTITY_BOOLEAN_FIELD))
        }
        return this
    }

    fun expectBooleanFieldDisplayToBeFalse(): VueCrudWebPage {
        expectElementTextToMatch(className(NEW_ENTITY_BOOLEAN_FIELD_DISPLAY), equalTo("false"))
        return this
    }

    fun clickSaveButton(): VueCrudWebPage {
        clickElement(className(SAVE_BUTTON))
        return this
    }

    fun expectEntity(username: String, date: LocalDate, offsetDateTime: OffsetDateTime, intField: Int, decimalField: BigDecimal, booleanField: Boolean, tags: List<String>, id: UUID? = null): VueCrudWebPage {
        expectElementToMatch({ getElements(className(USER_NAME)).map { it.text } }, hasItem(username))
        val entityIndex = getElements(className(USER_NAME)).map { it.text }.indexOf(username)
        expectElementTextToMatch(className(DATE), entityIndex, equalTo(DateTimeUtil.toIsoLocalDateString(date)))
        expectElementTextToMatch(className(TIME_STAMP), not(emptyOrNullString()))
        expectElementToMatch(
                {
                    DateTimeUtil.toUtcOffsetDateTime(getElements(className(TIME_STAMP))[entityIndex].text)
                },
                equalTo(offsetDateTime)
        )
        expectElementTextToMatch(className(INT_FIELD), entityIndex, equalTo(intField.toString()))
        expectElementTextToMatch(className(DECIMAL_FIELD), entityIndex, equalTo(decimalField.toString()))
        expectElementTextToMatch(className(BOOLEAN_FIELD), entityIndex, equalTo(booleanField.toString()))
        val displayedTags = getElements(className(TAGS))[entityIndex].findElements(className(TAG)).map { it.text }
        assertThat(displayedTags, containsInAnyOrder(*tags.toTypedArray()))
        expectElementTextToMatch(className(NUMBER), entityIndex, equalTo((entityIndex + 1).toString()))

        if (id != null) {
            expectElementTextToMatch(className(ID), entityIndex, equalTo(id.toString()))
        } else {
            expectElementTextToMatch(className(ID), entityIndex, not(emptyOrNullString()))
        }
        return this
    }

    fun clickDeleteEntityButton(username: String): VueCrudWebPage {
        val entityIndex = getElements(className(USER_NAME)).map { it.text }.indexOf(username)
        return clickElement(className(DELETE_ENTITY_BUTTON), entityIndex)
    }

    fun expectEntityNotToBeInTable(username: String): VueCrudWebPage {
        return expectElementToMatch(
                { getElements(className(USER_NAME)).map { it.text } },
                not(hasItem(username)),
                pollDelayInMillis = 500
        )
    }

    fun clickRefreshEntitiesIcon(): VueCrudWebPage {
        return clickElement(className(REFRESH_ENTITIES))
    }

    companion object {
        private const val PAGE_URL = "/vue/vue_crud"
        private const val NEW_ENTITY_TIMESTAMP_ISO_PRINT = "qa-epoch-timestamp-iso"
        private const val NEW_ENTITY_TIMESTAMP_FIELD = "qa-epoch-timestamp-field"
        private const val NEW_ENTITY_INT_FIELD = "qa-int-field"
        private const val NEW_ENTITY_DECIMAL_FIELD = "qa-decimal-field"
        private const val NEW_ENTITY_TAGS = "qa-tags-field"
        private const val NEW_ENTITY_USERNAME_FIELD = "qa-username-field"
        private const val NEW_ENTITY_DATE_FIELD = "qa-date-field"
        private const val NEW_ENTITY_BOOLEAN_FIELD = "qa-boolean-field"
        private const val NEW_ENTITY_BOOLEAN_FIELD_DISPLAY = "qa-boolean-field-display"
        private const val NEW_ENTITY_FORM = "qa-new-entity-form"
        private const val ADD_NEW_BUTTON = "qa-add-new-btn"
        private const val DONE_BUTTON = "qa-done-btn"
        private const val SAVE_BUTTON = "qa-save-btn"
        private const val TOTAL_ENTITIES = "qa-total-entities"
        private const val TOTAL_SERVER_ENTITIES = "qa-total-server-entities"
        private const val AUTO_REFRESH_SERVER_COUNT = "qa-auto-refresh-server-count"
        private const val REFRESH_ENTITIES = "qa-refresh-entities"
        private const val ID = "qa-id"
        private const val NUMBER = "qa-number"
        private const val DATE = "qa-date"
        private const val TIME_STAMP = "qa-timestamp"
        private const val INT_FIELD = "qa-display-int-field"
        private const val BOOLEAN_FIELD = "qa-display-boolean-field"
        private const val DECIMAL_FIELD = "qa-display-decimal-field"
        private const val USER_NAME = "qa-username"
        private const val TAGS = "qa-tags"
        private const val TAG = "qa-tag"
        private const val LOADING_MESSAGE = "qa-loading-message"
        private const val REFRESHING_ENTITIES_MESSAGE = "qa-refreshing-entities-message"
        private const val DELETE_ENTITY_BUTTON = "qa-delete-entity-btn"
    }
}