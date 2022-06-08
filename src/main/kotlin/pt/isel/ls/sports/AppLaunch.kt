package pt.isel.ls.sports

import pt.isel.ls.sports.database.AppMemoryDB
import pt.isel.ls.sports.database.AppMemoryDBSource
import pt.isel.ls.sports.database.AppPostgresDB

const val PORT_ENV = "PORT"
const val DEFAULT_PORT = 8888
const val JDBC_DATABASE_URL_ENV = "JDBC_DATABASE_URL"

/**
 * Sports API application's entry point.
 */
fun main() {
    val jdbcDatabaseURL: String? = System.getenv(JDBC_DATABASE_URL_ENV)
    val port = System.getenv(PORT_ENV)?.toIntOrNull() ?: DEFAULT_PORT

    val database = if (jdbcDatabaseURL != null)
        AppPostgresDB(jdbcDatabaseURL)
    else
        AppMemoryDB(AppMemoryDBSource())

    AppServer(port, database).also { it.start() }
}
