package pt.isel.ls.sports.services.utils

/**
 * Validates a serial ID.
 * @param id The serial ID to validate.
 * @return true if the ID is valid, false otherwise.
 */
fun isValidId(id: Int) =
    id > 0
