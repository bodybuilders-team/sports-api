package pt.isel.ls.sports.services.exceptions

/**
 * Thrown when a user is not authorized to do an operation.
 *
 * @param message exception message
 */
class AuthorizationException(message: String? = null) : Exception(message)
