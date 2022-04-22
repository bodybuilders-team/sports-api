package pt.isel.ls.sports.utils

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
