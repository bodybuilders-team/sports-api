package pt.isel.ls.sports.services

/**
 * Thrown if a user is not authenticated.
 *
 * @param message exception message
 */
class AuthenticationException(message: String? = null) : Exception(message)

/**
 * Thrown if an argument is invalid.
 *
 * @param message exception message
 */
class InvalidArgumentException(message: String? = null) : Exception(message)

/**
 * Thrown when a user is not authorized to do an operation.
 *
 * @param message exception message
 */
class AuthorizationException(message: String? = null) : Exception(message)
