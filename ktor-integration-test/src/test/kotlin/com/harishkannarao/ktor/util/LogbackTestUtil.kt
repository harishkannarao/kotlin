package com.harishkannarao.ktor.util

import ch.qos.logback.classic.Logger
import ch.qos.logback.classic.LoggerContext
import ch.qos.logback.classic.encoder.PatternLayoutEncoder
import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.FileAppender
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import org.slf4j.LoggerFactory
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*

class LogbackTestUtil(
        loggerName: String,
        loggingPattern: String = "[%X{requestId}] %level %message%n"
) {

    private val logFileLocation: String = "build/logs"
    private val logFilePrefix: String = "test-log"
    private val logger: Logger = LoggerFactory.getLogger(loggerName) as Logger
    private val context: LoggerContext = LoggerFactory.getILoggerFactory() as LoggerContext
    private val encoder: PatternLayoutEncoder = createPatternLayoutEncoder(loggingPattern, context)

    private lateinit var testAppender: FileAppender<ILoggingEvent>
    private lateinit var logFile: String

    fun setUp() {
        logFile = "$logFileLocation/$logFilePrefix-${UUID.randomUUID()}.log"

        testAppender = FileAppender()
        testAppender.file = Paths.get(logFile).toAbsolutePath().toString()
        testAppender.name = "TEST_APPENDER_${UUID.randomUUID()}"
        testAppender.context = context
        testAppender.encoder = encoder
        testAppender.start()

        logger.addAppender(testAppender)
    }

    fun tearDown() {
        logger.detachAppender(testAppender)
    }

    fun assertLogEntry(expectedString: String) {
        val matchFound = Files.lines(Paths.get(logFile).toAbsolutePath()).anyMatch { s -> s.contains(expectedString) }
        assertThat("Log file: ${Paths.get(logFile).toAbsolutePath()} does not contain expected string $expectedString", matchFound, equalTo(true))
    }

    fun assertLogEntryUsingRegEx(regEx: String) {
        val matchFound = Files.lines(Paths.get(logFile).toAbsolutePath())
                .anyMatch { s -> regEx.toRegex().containsMatchIn(s) }
        assertThat("Log file: ${Paths.get(logFile).toAbsolutePath()} does not contain expected regEx: '$regEx'", matchFound, equalTo(true))
    }

    private fun createPatternLayoutEncoder(loggingPattern: String, context: LoggerContext): PatternLayoutEncoder {
        val encoder = PatternLayoutEncoder()
        encoder.pattern = loggingPattern
        encoder.context = context
        encoder.start()
        return encoder
    }
}