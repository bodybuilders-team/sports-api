package pt.isel.ls.sports

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.http4k.core.Response
import org.http4k.core.Status

/**
 * Thrown when something was not found.
 * @property message exception cause
 */
class NotFoundException(override val message: String) : Exception(message)

@Serializable
data class AppError(val code: Int, val error: String, val description: String, val extraInfo: String? = null) {

    companion object {
        val BAD_REQUEST = AppError(1000, "BAD_REQUEST", "The request was malformed")
        val NOT_FOUND = AppError(1001, "NOT_FOUND", "The requested resource was not found")
        val DATABASE_ERROR = AppError(1002, "DATABASE_ERROR", "There was an error accessing the database")
        val INTERNAL_ERROR = AppError(1003, "INTERNAL_ERROR", "There was an internal error")
        val INVALID_CREDENTIALS = AppError(1004, "INVALID_CREDENTIALS", "The provided credentials are invalid")
        val NO_CREDENTIALS = AppError(1005, "NO_CREDENTIALS", "No credentials were provided")
    }
}

fun AppError.getStatus(): Status = when (this) {
    AppError.BAD_REQUEST -> Status.BAD_REQUEST
    AppError.INVALID_CREDENTIALS -> Status.UNAUTHORIZED

    AppError.NOT_FOUND -> Status.NOT_FOUND
    AppError.DATABASE_ERROR -> Status.INTERNAL_SERVER_ERROR

    else -> Status.INTERNAL_SERVER_ERROR
}

fun errorResponse(error: Throwable): Response = when (error) {
    is NotFoundException -> errorResponse(AppError.NOT_FOUND)
    else -> errorResponse(AppError.INTERNAL_ERROR)
}

fun errorResponse(error: AppError, errorMessage: (() -> String)? = null): Response {
    val newError = if (errorMessage != null)
        error.copy(extraInfo = errorMessage())
    else
        error

    return Response(newError.getStatus()).body(Json.encodeToString(newError))
}


