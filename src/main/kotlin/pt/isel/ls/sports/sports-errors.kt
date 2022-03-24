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


/**
 * Represents an application error.
 * @property code error code
 * @property name error name
 * @property description short description of the error
 * @property extraInfo other info related to the error
 */
@Serializable
data class AppError(val code: Int, val name: String, val description: String, var extraInfo: String? = null) {

	companion object {
		fun badRequest(extraInfo: String? = null) =
			AppError(1000, "BAD_REQUEST", "The request was malformed", extraInfo)

		fun notFound(extraInfo: String? = null) =
			AppError(1001, "NOT_FOUND", "The requested resource was not found", extraInfo)

		fun databaseError(extraInfo: String? = null) =
			AppError(1002, "DATABASE_ERROR", "There was an error accessing the database", extraInfo)

		fun internalError(extraInfo: String? = null) =
			AppError(1003, "INTERNAL_ERROR", "There was an internal error", extraInfo)

		fun invalidCredentials(extraInfo: String? = null) =
			AppError(1004, "INVALID_CREDENTIALS", "The provided credentials are invalid", extraInfo)

		fun noCredentials(extraInfo: String? = null) =
			AppError(1005, "NO_CREDENTIALS", "No credentials were provided", extraInfo)
	}

	/**
	 * Gets the HTTP status for each AppError.
	 * @return HTTP status
	 */
	private fun getStatus(): Status = when (this) {
		badRequest() -> Status.BAD_REQUEST
		invalidCredentials() -> Status.UNAUTHORIZED
		notFound() -> Status.NOT_FOUND
		databaseError() -> Status.INTERNAL_SERVER_ERROR
		else -> Status.INTERNAL_SERVER_ERROR
	}


	/**
	 * Converts the AppError to an HTTP Response
	 * @return HTTP response
	 */
	fun toResponse() = Response(status = getStatus()).body(Json.encodeToString(this))
}


/**
 * Gets the HTTP response associated with the [error]
 * @param error application error
 * @return HTTP response
 */
fun getErrorResponse(error: Throwable): Response =
	when (error) {
		is NotFoundException -> AppError.notFound()
		else -> AppError.internalError()
	}.toResponse()
