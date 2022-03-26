package pt.isel.ls.sports.errors

import kotlinx.serialization.SerializationException
import org.http4k.core.Response

/**
 * Gets the HTTP response associated with the [error]
 * @param error application error
 * @return HTTP response
 */
fun getErrorResponse(error: Throwable): Response =
    when (error) {
        is SerializationException -> SportsError.badRequest(error.message)
        is SportsError -> error
        else -> SportsError.internalError(error.message)
    }.toResponse()
