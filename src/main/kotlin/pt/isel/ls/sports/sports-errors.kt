package pt.isel.ls.sports

import java.lang.Exception


/**
 * Thrown when something was not found.
 * @property message exception cause
 */
class NotFoundException(override val message: String) : Exception(message)
