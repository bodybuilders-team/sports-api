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
        is SerializationException -> AppError.badRequest(error.message)
        is AppError -> error
        else -> AppError.internalError(error.message)
    }.toResponse()
