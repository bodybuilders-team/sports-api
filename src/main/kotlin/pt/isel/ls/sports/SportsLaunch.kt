package pt.isel.ls.sports

import pt.isel.ls.sports.data.SportsDataMem

/**
 * Sports API application's entry point.
 */
fun main() {
    val port = System.getenv("PORT")?.toIntOrNull() ?: 8888
    val dataSource = SportsDataMem()
    val server = SportsServer(port, dataSource).also { it.start() }
    readln()
    server.stop()
}
