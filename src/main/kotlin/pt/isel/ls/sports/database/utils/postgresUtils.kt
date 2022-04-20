package pt.isel.ls.sports.database.utils

import kotlinx.datetime.LocalDate
import kotlinx.datetime.toJavaLocalDate
import pt.isel.ls.sports.errors.AppException
import pt.isel.ls.sports.utils.Logger
import java.io.File
import java.sql.Connection
import java.sql.Date
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

/**
 * Rollbacks a transaction and logs the error.
 * @throws AppException.InternalError if the rollback failed.
 */
fun rollbackTransaction(conn: Connection) {
    runCatching {
        Logger.warn("Transaction is being rolled back")
        conn.rollback()
    }.getOrElse {
        Logger.error("Could not rollback transaction")
        throw AppException.InternalError()
    }
}

/**
 * Runs SQL script
 * @param filepath path of the script to run
 */
fun Connection.runScript(filepath: String) {
    File(filepath)
        .readText()
        .also {
            this.prepareStatement(it)
                .executeUpdate()
        }
}

/**
 * Converts a [LocalDate] to a [Date].
 *
 * @param date the date to convert
 * @return the converted date
 */
fun getSQLDate(date: LocalDate) = Date.valueOf(date.toJavaLocalDate())
