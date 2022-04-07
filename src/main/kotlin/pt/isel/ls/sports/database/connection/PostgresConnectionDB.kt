package pt.isel.ls.sports.database.connection

import java.io.Closeable
import java.sql.Connection

/**
 * Postgres database connection implementation.
 */
class PostgresConnectionDB(val connection: Connection) : ConnectionDB, Closeable {

    override fun close() {
        connection.close()
    }
}

/**
 * Converts a [ConnectionDB] instance to a [Connection] instance
 */
fun ConnectionDB.getPostgresConnection(): Connection =
    (this as PostgresConnectionDB).connection