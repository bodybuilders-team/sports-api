package pt.isel.ls.sports

import pt.isel.ls.sports.data.SportsDataMem

const val PORT_ENV = "PORT"
const val DEFAULT_PORT = 8888

/**
 * Sports API application's entry point.
 */
fun main() {
    val port = System.getenv(PORT_ENV)?.toIntOrNull() ?: DEFAULT_PORT
    val dataSource = SportsDataMem()

    val server = SportsServer(port, dataSource).also { it.start() }

    readln()
    server.stop()

    logger.info("leaving Main")
}
