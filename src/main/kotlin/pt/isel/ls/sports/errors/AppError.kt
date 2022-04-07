package pt.isel.ls.sports.errors

/**
 * Represents an application error.
 * @property code error code
 * @property name error name
 * @property description short description of the error
 * @property extraInfo other info related to the error
 */
// TODO: 07/04/2022 Separate web api errors from services, database and other errors
open class AppError(val code: Int, val name: String, val description: String, val extraInfo: String? = null) :
    Exception() {

    class BadRequest(extraInfo: String? = null) :
        AppError(1000, "BAD_REQUEST", "The request was malformed", extraInfo)

    class NotFound(extraInfo: String? = null) :
        AppError(1001, "NOT_FOUND", "The requested resource was not found", extraInfo)

    class InternalError(extraInfo: String? = null) :
        AppError(1002, "INTERNAL_ERROR", "There was an internal error", extraInfo)

    class InvalidCredentials(extraInfo: String? = null) :
        AppError(1003, "INVALID_CREDENTIALS", "The provided credentials are invalid", extraInfo)

    class NoCredentials(extraInfo: String? = null) :
        AppError(1004, "NO_CREDENTIALS", "No credentials were provided", extraInfo)

    class InvalidArgument(extraInfo: String? = null) :
        AppError(1005, "INVALID_ARGUMENT", "An argument is invalid", extraInfo)

    class Forbidden(extraInfo: String? = null) :
        AppError(1006, "FORBIDDEN", "User is not authorized", extraInfo)

    class Conflict(extraInfo: String? = null) :
        AppError(1007, "CONFLICT", "There was a conflict", extraInfo)

    class DatabaseError(extraInfo: String? = null) :
        AppError(1008, "DATABASE_ERROR", "There was a database error", extraInfo)

    /**
     * Needed for comparing AppErrors based on code only.
     */
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is AppError) return false

        if (this.code != other.code) return false

        return true
    }

    /**
     * Needed when overriding equals.
     */
    override fun hashCode(): Int {
        var result = code
        result = 31 * result + name.hashCode()
        result = 31 * result + description.hashCode()
        result = 31 * result + (extraInfo?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "AppError(code=$code, name='$name', description='$description', extraInfo=$extraInfo)"
    }
}
