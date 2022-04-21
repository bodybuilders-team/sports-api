package pt.isel.ls.sports.api.utils

import kotlinx.serialization.SerializationException
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.http4k.core.Response
import org.http4k.core.Status
import pt.isel.ls.sports.errors.AppException
import pt.isel.ls.sports.utils.Logger

/**
 * Gets the HTTP status for each AppException.
 * @return HTTP status
 */
private fun AppException.getStatus(): Status = when (this) {
    is AppException.BadRequest -> Status.BAD_REQUEST
    is AppException.NotFound -> Status.NOT_FOUND
    is AppException.InternalError -> Status.INTERNAL_SERVER_ERROR
    is AppException.InvalidCredentials -> Status.UNAUTHORIZED
    is AppException.NoCredentials -> Status.BAD_REQUEST
    is AppException.InvalidArgument -> Status.BAD_REQUEST
    is AppException.Forbidden -> Status.FORBIDDEN
    is AppException.Conflict -> Status.CONFLICT
    else -> Status.INTERNAL_SERVER_ERROR
}

/**
 * Converts the AppException to an HTTP Response.
 *
 * @return HTTP response
 */
fun AppException.toResponse() = Response(status = getStatus()).body(Json.encodeToString(AppErrorDTO(this)))

/**
 * Runs the given function and returns the result as a Response.
 * Catches any exceptions and returns an error response accordingly.
 */
inline fun runAndCatch(block: () -> Response): Response =
    try {
        block()
    } catch (error: SerializationException) {

        Logger.warn(error.toString())
        AppException.BadRequest(error.localizedMessage).toResponse()
    } catch (error: AppException.DatabaseError) {

        Logger.error("${error.extraInfo}\n${error.stackTraceToString()}")
        AppException.InternalError().toResponse()
    } catch (error: AppException.InternalError) {

        Logger.error(error.stackTraceToString())
        error.toResponse()
    } catch (error: AppException) {

        Logger.warn(error.toString())
        error.toResponse()
    } catch (error: Exception) {

        Logger.error(error.stackTraceToString())
        AppException.InternalError().toResponse()
    }
