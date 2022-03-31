package pt.isel.ls.sports

import org.http4k.core.Request
import org.slf4j.Logger
import org.slf4j.LoggerFactory

const val LOGGER_NAME = "pt.isel.ls.sports.Logger"

val logger: Logger = LoggerFactory.getLogger(LOGGER_NAME)

/**
 * Logs a request.
 *
 * @param request request to log
 */
fun logRequest(request: Request) {
    logger.info(
        "incoming request: method={}, uri={}, content-type={} accept={}",
        request.method,
        request.uri,
        request.header("content-type"),
        request.header("accept")
    )
}
