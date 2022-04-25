package pt.isel.ls.sports.database.connection

import java.sql.Connection

/**
 * Database connection representation.
 */
interface ConnectionDB {

    /**
     * Converts a [ConnectionDB] instance to a [Connection] instance.
     *
     * @return the [Connection] instance
     */
    fun getPostgresConnection(): Connection = (this as PostgresConnectionDB).connection
}
