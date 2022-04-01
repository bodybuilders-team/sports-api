package pt.isel.ls.sports.database.postgres

import org.postgresql.ds.PGSimpleDataSource
import pt.isel.ls.sports.errors.AppError
import java.sql.Connection
import java.sql.SQLException

abstract class AbstractPostgresDB(protected val dataSource: PGSimpleDataSource) {
    /**
     * Gets a connection and uses it with [block].
     * @param block a function to process in use
     * @return the result of block function invoked
     * @throws AppError.DatabaseError if an error occurs while accessing the database
     */
    protected inline fun <R> useConnection(block: (connection: Connection) -> R): R =
        runCatching {
            dataSource.connection
        }.getOrElse {
            when (it) {
                is SQLException ->
                    throw AppError.DatabaseError("Error accessing database")
                else -> throw it
            }
        }.use(block)
}
