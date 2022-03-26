package pt.isel.ls.sports.data

/**
 * Possible values of the OrderBy clause in PostgreSQL.
 */
enum class SortOrder(val str: String) {
    ASCENDING("ASC"),
    DESCENDING("DESC")
}
