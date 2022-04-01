package pt.isel.ls.sports.errors

import kotlinx.serialization.Serializable

/**
 * Represents an application error.
 * @property code error code
 * @property name error name
 * @property description short description of the error
 * @property extraInfo other info related to the error
 */
@Serializable
sealed class AppError(val code: Int, val name: String, val description: String) :
    Exception() {

    abstract val extraInfo: String?

    /**
     * Error when the request is malformed.
     */
    @Serializable
    class BadRequest(override val extraInfo: String? = null) :
        AppError(1000, "BAD_REQUEST", "The request was malformed")

    @Serializable
    class NotFound(override val extraInfo: String? = null) :
        AppError(1001, "NOT_FOUND", "The requested resource was not found")

    @Serializable
    class DatabaseError(override val extraInfo: String? = null) :
        AppError(1002, "DATABASE_ERROR", "There was an error accessing the database")

    @Serializable
    class InternalError(override val extraInfo: String? = null) :
        AppError(1003, "INTERNAL_ERROR", "There was an internal error")

    @Serializable
    class InvalidCredentials(override val extraInfo: String? = null) :
        AppError(1004, "INVALID_CREDENTIALS", "The provided credentials are invalid")

    @Serializable
    class NoCredentials(override val extraInfo: String? = null) :
        AppError(1005, "NO_CREDENTIALS", "No credentials were provided")

    @Serializable
    class InvalidArgument(override val extraInfo: String? = null) :
        AppError(1006, "INVALID_ARGUMENT", "An argument is invalid")

    @Serializable
    class Forbidden(override val extraInfo: String? = null) :
        AppError(1007, "FORBIDDEN", "User is not authorized")

    @Serializable
    class Conflict(override val extraInfo: String? = null) :
        AppError(1008, "CONFLICT", "There was a conflict")
}
