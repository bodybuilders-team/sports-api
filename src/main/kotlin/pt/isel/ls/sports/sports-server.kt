package pt.isel.ls.sports

import org.http4k.server.Http4kServer
import org.http4k.server.Jetty
import org.http4k.server.asServer
import pt.isel.ls.sports.data.SportsDataMem

class SportsServer(private val port: Int) {

    private val server: Http4kServer

    init {
        val db = SportsDataMem()
        val services = SportsServices(db)
        val webApi = SportsWebApi(services)
        server = webApi.getApp().asServer(Jetty(port))
    }

    fun start() {
        println("Starting server on port $port")
        server.start()
    }

    fun stop() {
        println("Stopping server")
        server.stop()
    }
}

fun main() {
    val server = SportsServer(8080)
    server.start()
    readln()
    server.stop()
}

