package pt.isel.ls.sports.database.utils

/**
 * Possible values of the OrderBy clause in PostgreSQL.
 */
enum class SortOrder(val str: String) {
    ASCENDING("ASC"),
    DESCENDING("DESC");

    companion object {

        /**
         * Parses a string into a SortOrder or null.
         *
         * @param sortOrder string to parse, expected to be lowercase
         * @return a [SortOrder] if the string was a valid value or null if it was invalid
         */
        fun parse(sortOrder: String): SortOrder? =
            values().firstOrNull { it.name.lowercase() == sortOrder }
    }
}
