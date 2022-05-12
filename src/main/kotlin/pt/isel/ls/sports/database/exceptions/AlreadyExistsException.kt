package pt.isel.ls.sports.database.exceptions

/**
 * Thrown when a resource already exists.
 *
 * @param message exception message
 */
class AlreadyExistsException(message: String? = null) : Exception(message)
