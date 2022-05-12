package pt.isel.ls.sports.utils

import org.http4k.core.Request
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.sql.Timestamp

/**
 * Logger utility class.
 */
object Logger {
    private const val LOGGER_NAME = "pt.isel.ls.sports.Logger"
    private val logger: Logger = LoggerFactory.getLogger(LOGGER_NAME)

    /**
     * Log a message at ERROR level with a timestamp.
     *
     * @param message the message to log
     */
    fun error(message: String) =
        logger.error("{} : {}", Timestamp(System.currentTimeMillis()), message)

    /**
     * Log a message at WARN level with a timestamp.
     *
     * @param message the message to log
     */
    fun warn(message: String) =
        logger.warn("{} : {}", Timestamp(System.currentTimeMillis()), message)

    /**
     * Log a message at TRACE level with a timestamp.
     *
     * @param message the message to log
     */
    fun trace(message: String) =
        logger.trace("{} : {}", Timestamp(System.currentTimeMillis()), message)

    /**
     * Log a message at DEBUG level with a timestamp.
     *
     * @param message the message to log
     */
    @Suppress("unused")
    fun debug(message: String) =
        logger.debug("{} : {}", Timestamp(System.currentTimeMillis()), message)

    /**
     * Log a message at INFO level with a timestamp.
     *
     * @param message the message to log
     */
    fun info(message: String) =
        logger.info("{} : {}", Timestamp(System.currentTimeMillis()), message)

    /**
     * Logs a request.
     *
     * @param request request to log
     */
    fun logRequest(request: Request) =
        logger.info(
            "{} : Incoming Request: method={}, uri={}, content-type={} accept={}",
            Timestamp(System.currentTimeMillis()),
            request.method,
            request.uri,
            request.header("content-type"),
            request.header("accept")
        )
}
