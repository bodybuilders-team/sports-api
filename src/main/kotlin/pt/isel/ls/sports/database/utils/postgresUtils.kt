package pt.isel.ls.sports.database.utils

import java.sql.PreparedStatement
import java.sql.Types

/**
 * Sets the designated string to the given value or null.
 *
 * @param index the first parameter is 1, the second is 2, ...
 * @param value the parameter value
 */
fun PreparedStatement.setStringOrNull(index: Int, value: String?) =
    when (value) {
        null -> setNull(index, Types.VARCHAR)
        else -> setString(index, value)
    }

/**
 * Sets the designated int to the given value or null.
 *
 * @param index the first parameter is 1, the second is 2, ...
 * @param value the parameter value
 */
fun PreparedStatement.setIntOrNull(index: Int, value: Int?) =
    when (value) {
        null -> setNull(index, Types.INTEGER)
        else -> setInt(index, value)
    }
