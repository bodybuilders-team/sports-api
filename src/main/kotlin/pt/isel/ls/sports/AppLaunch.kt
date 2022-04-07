package pt.isel.ls.sports

import pt.isel.ls.sports.database.AppPostgresDB
import pt.isel.ls.sports.utils.Logger

const val PORT_ENV = "PORT"
const val DEFAULT_PORT = 8888
const val JDBC_DATABASE_URL_ENV = "JDBC_DATABASE_URL"

/**
 * Sports API application's entry point.
 */
fun main() {
    val jdbcDatabaseURL: String = System.getenv(JDBC_DATABASE_URL_ENV)
    val port = System.getenv(PORT_ENV)?.toIntOrNull() ?: DEFAULT_PORT

    val database = AppPostgresDB(jdbcDatabaseURL)

    val server = AppServer(port, database).also { it.start() }

    readln()
    server.stop()

    Logger.info("Leaving main")
}
