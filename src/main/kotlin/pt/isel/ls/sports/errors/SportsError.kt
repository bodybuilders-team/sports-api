@file:Suppress("EqualsOrHashCode")

package pt.isel.ls.sports.errors

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.http4k.core.Response
import org.http4k.core.Status

/**
 * Represents an application error.
 * @property code error code
 * @property name error name
 * @property description short description of the error
 * @property extraInfo other info related to the error
 */
@Serializable
data class SportsError(val code: Int, val name: String, val description: String, var extraInfo: String? = null) :
    Exception() {

    companion object {
        fun badRequest(extraInfo: String? = null) =
            SportsError(1000, "BAD_REQUEST", "The request was malformed", extraInfo)

        fun notFound(extraInfo: String? = null) =
            SportsError(1001, "NOT_FOUND", "The requested resource was not found", extraInfo)

        fun databaseError(extraInfo: String? = null) =
            SportsError(1002, "DATABASE_ERROR", "There was an error accessing the database", extraInfo)

        fun internalError(extraInfo: String? = null) =
            SportsError(1003, "INTERNAL_ERROR", "There was an internal error", extraInfo)

        fun invalidCredentials(extraInfo: String? = null) =
            SportsError(1004, "INVALID_CREDENTIALS", "The provided credentials are invalid", extraInfo)

        fun noCredentials(extraInfo: String? = null) =
            SportsError(1005, "NO_CREDENTIALS", "No credentials were provided", extraInfo)

        fun invalidArgument(extraInfo: String? = null): Throwable =
            SportsError(1006, "INVALID_ARGUMENT", "An argument is invalid", extraInfo)

        fun forbidden(extraInfo: String? = null): Throwable =
            SportsError(1007, "FORBIDDEN", "User is not authorized", extraInfo)
    }

    /**
     * Gets the HTTP status for each AppError.
     * @return HTTP status
     */
    private fun getStatus(): Status = when (this) {
        badRequest() -> Status.BAD_REQUEST
        notFound() -> Status.NOT_FOUND
        databaseError() -> Status.INTERNAL_SERVER_ERROR
        internalError() -> Status.INTERNAL_SERVER_ERROR
        invalidCredentials() -> Status.UNAUTHORIZED
        noCredentials() -> Status.BAD_REQUEST
        invalidArgument() -> Status.BAD_REQUEST
        forbidden() -> Status.FORBIDDEN
        else -> Status.INTERNAL_SERVER_ERROR
    }

    /**
     * Converts the AppError to an HTTP Response
     * @return HTTP response
     */
    fun toResponse() = Response(status = getStatus()).body(Json.encodeToString(this))

    /**
     * Needed for comparing AppErrors based on code only.
     */
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other?.javaClass != javaClass) return false

        other as SportsError

        if (this.code != other.code) return false

        return true
    }
}
