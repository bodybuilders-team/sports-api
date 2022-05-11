package pt.isel.ls.sports.database

/**
 * Thrown if the database cannot be accessed.
 */
class DatabaseAccessException : Exception()

/**
 * Thrown if a database rollback fails.
 */
class DatabaseRollbackException : Exception()

/**
 * Thrown when a resource was not found.
 *
 * @param message exception message
 */
class NotFoundException(message: String? = null) : Exception(message)

/**
 * Thrown when a resource already exists.
 *
 * @param message exception message
 */
class AlreadyExistsException(message: String? = null) : Exception(message)

/**
 * Thrown if an argument is invalid.
 *
 * @param message exception message
 */
class InvalidArgumentException(message: String? = null) : Exception(message)
