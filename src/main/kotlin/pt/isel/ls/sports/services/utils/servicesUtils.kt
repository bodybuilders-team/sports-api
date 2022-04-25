package pt.isel.ls.sports.services.utils

/**
 * Validates an ID.
 * An ID is valid if it is not negative.
 *
 * @param id ID to validate
 * @return true if the ID is valid, false otherwise
 */
fun isValidId(id: Int) =
    id >= 0
