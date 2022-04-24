package pt.isel.ls.sports.utils

import org.http4k.core.Request
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.sql.Timestamp

// TODO: 07/04/2022 - Double check all log levels so they are appropriate, and add more logs
/**
 * Logger utility class.
 */
object Logger {
    private const val LOGGER_NAME = "pt.isel.ls.sports.Logger"
    private val logger: Logger = LoggerFactory.getLogger(LOGGER_NAME)

    fun error(message: String) =
        logger.error("{} : {}", Timestamp(System.currentTimeMillis()), message)

    fun warn(message: String) =
        logger.warn("{} : {}", Timestamp(System.currentTimeMillis()), message)

    fun trace(message: String) =
        logger.trace("{} : {}", Timestamp(System.currentTimeMillis()), message)

    fun debug(message: String) =
        logger.debug("{} : {}", Timestamp(System.currentTimeMillis()), message)

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
