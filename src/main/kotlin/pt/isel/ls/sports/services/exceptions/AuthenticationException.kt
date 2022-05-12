package pt.isel.ls.sports.services.exceptions

/**
 * Thrown if a user is not authenticated.
 *
 * @param message exception message
 */
class AuthenticationException(message: String? = null) : Exception(message)
