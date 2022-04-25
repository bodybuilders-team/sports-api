package pt.isel.ls.sports.database.connection

import java.io.Closeable
import java.sql.Connection

/**
 * Postgres database connection implementation.
 *
 * @property connection the connection to the database
 */
class PostgresConnectionDB(val connection: Connection) : ConnectionDB, Closeable {

    override fun close() {
        connection.close()
    }
}
