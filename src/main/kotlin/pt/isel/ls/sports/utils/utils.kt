package pt.isel.ls.sports.utils

import pt.isel.ls.sports.errors.AppException

/**
 * Returns a substring of this string that starts at the specified [startIndex] and continues to the end of the string.
 *
 * @return substring or null if the [startIndex] is greater or equal than the string length
 */
fun String.substringOrNull(startIndex: Int): String? =
    if (this.length > startIndex)
        this.substring(startIndex)
    else
        null

/**
 * Parses the string as an Int number and returns the result.
 *
 * @throws AppException if the string is not a valid representation of a number.
 */
fun String.toIntOrThrow(errorInfo: (() -> String)? = null): Int =
    this.toIntOrNull()
        ?: throw AppException.BadRequest(errorInfo?.invoke() ?: "Error parsing $this to Int")
