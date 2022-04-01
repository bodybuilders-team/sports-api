package pt.isel.ls.sports.api.utils

import kotlinx.serialization.SerializationException
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.http4k.core.Response
import org.http4k.core.Status
import pt.isel.ls.sports.errors.AppError

/**
 * Gets the HTTP status for each AppError.
 * @return HTTP status
 */
private fun AppError.getStatus(): Status = when (this) {
    is AppError.BadRequest -> Status.BAD_REQUEST
    is AppError.NotFound -> Status.NOT_FOUND
    is AppError.DatabaseError -> Status.INTERNAL_SERVER_ERROR
    is AppError.InternalError -> Status.INTERNAL_SERVER_ERROR
    is AppError.InvalidCredentials -> Status.UNAUTHORIZED
    is AppError.NoCredentials -> Status.BAD_REQUEST
    is AppError.InvalidArgument -> Status.BAD_REQUEST
    is AppError.Forbidden -> Status.FORBIDDEN
    is AppError.Conflict -> Status.CONFLICT
    else -> Status.INTERNAL_SERVER_ERROR
}

/**
 * Converts the AppError to an HTTP Response
 * @return HTTP response
 */
fun AppError.toResponse() = Response(status = getStatus()).body(Json.encodeToString(this))

/**
 * Gets the HTTP response associated with the [error]
 * @param error application error
 * @return HTTP response
 */
fun getErrorResponse(error: Throwable): Response =
    when (error) {
        is SerializationException -> AppError.BadRequest(error.message)
        is AppError -> error
        else -> AppError.InternalError(error.message)
    }.toResponse()
