package pt.isel.ls.sports.services.utils

/**
 * Validates a ID.
 * @param id The ID to validate.
 * @return true if the ID is valid, false otherwise.
 */
fun isValidId(id: Int) =
    id >= 0
