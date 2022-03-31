package pt.isel.ls.sports

import pt.isel.ls.sports.data.AppDataMem
import pt.isel.ls.sports.data.AppDataMemSource

const val PORT_ENV = "PORT"
const val DEFAULT_PORT = 8888
const val JDBC_DATABASE_URL_ENV = "JDBC_DATABASE_URL"

/**
 * Sports API application's entry point.
 */
fun main() {
    val port = System.getenv(PORT_ENV)?.toIntOrNull() ?: DEFAULT_PORT
    val database = AppDataMem(AppDataMemSource())

    val server = AppServer(port, database).also { it.start() }

    readln()
    server.stop()

    logger.info("leaving Main")
}
