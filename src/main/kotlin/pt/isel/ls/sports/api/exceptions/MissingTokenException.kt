package pt.isel.ls.sports.api.exceptions

/**
 * Thrown when the token is not present in the request
 *
 * @param message exception message
 */
class MissingTokenException(message: String? = null) : Exception(message)
