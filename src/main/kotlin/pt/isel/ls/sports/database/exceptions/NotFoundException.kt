package pt.isel.ls.sports.database.exceptions

/**
 * Thrown when a resource was not found.
 *
 * @param message exception message
 */
class NotFoundException(message: String? = null) : Exception(message)
